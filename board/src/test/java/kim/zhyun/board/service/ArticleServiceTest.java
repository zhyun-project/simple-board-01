package kim.zhyun.board.service;

import kim.zhyun.board.data.ArticleCreateRequest;
import kim.zhyun.board.data.ArticleDto;
import kim.zhyun.board.data.ArticleUpdateRequest;
import kim.zhyun.board.domain.Article;
import kim.zhyun.board.exception.ArticleNotFoundException;
import kim.zhyun.board.repository.ArticleRepository;
import kim.zhyun.board.data.type.ExceptionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class ArticleServiceTest {
    private final ArticleService service;
    private final ArticleRepository repository;
    
    public ArticleServiceTest(@Autowired ArticleService service,
                              @Autowired ArticleRepository repository) {
        this.service = service;
        this.repository = repository;
    }
    
    @DisplayName("전체 게시글 조회")
    @Test
    void findAll() {
        // when - then
        assertThat(service.findAll())
                .usingRecursiveComparison()
                .isEqualTo(repository.findAll().stream()
                        .map(ArticleDto::from)
                        .toList());
    }
    
    @DisplayName("없는 게시글 번호 조회 - ArticleNotFound Exception")
    @Test
    void findByIdWithArticleNotFoundException() {
        // given
        long articleId = Long.MAX_VALUE;
        
        // when - then
        assertThrows(ArticleNotFoundException.class,
                () -> service.findById(articleId),
                ExceptionType.ARTICLE_NOT_FOUND.getDescription());
    }
    
    @DisplayName("게시글 번호 조회 - 성공")
    @Test
    void findById() {
        // given
        long articleId = service.findAll().get(3).getId();
        
        // when - then
        assertThat(service.findById(articleId))
                .usingRecursiveComparison()
                .isEqualTo(ArticleDto.from(repository.findById(articleId).orElseGet(Article::new)));
    }
    
    @DisplayName("게시글 등록")
    @Test
    void save() {
        // given
        ArticleCreateRequest request = ArticleCreateRequest.of("테스트입니다.", "안뇽하세요? 감기 안걸리도록 기원하는 글 🙏");
        
        // when
        List<Article> beforeArticles = repository.findAll();
        long savedId = service.save(request);
        
        // then
        List<Long> saveBeforeArticleIds = beforeArticles.stream().map(Article::getId).toList();
        ArticleDto saved = service.findById(savedId);
        ArticleCreateRequest actual = ArticleCreateRequest.of(saved.getTitle(), saved.getContent());
        
        assertThat(savedId).isNotIn(saveBeforeArticleIds);
        assertThat(actual).usingRecursiveComparison().isEqualTo(request);
    }
    
    
    @DisplayName("게시글 수정 - 실패 : 없는 게시글 번호 접근")
    @Test
    void update_fail_in_non_existent_id() {
        // given
        ArticleUpdateRequest request = ArticleUpdateRequest.of(Long.MAX_VALUE, "버그내야지", "🐛");
        
        // when - then
        assertThrows(ArticleNotFoundException.class,
                () -> service.update(request),
                ExceptionType.ARTICLE_NOT_FOUND.getDescription());
    }
    
    
    @DisplayName("게시글 수정 - 성공")
    @Test
    void update() {
        // given
        long id = service.findAll().get(7).getId();
        ArticleDto beforeArticle = service.findById(id);
        ArticleUpdateRequest request = ArticleUpdateRequest.of(id, "게시글 수정됨", "🔨🔧🪛");
        
        // when
        service.update(request);
        repository.flush();
        
        // then
        ArticleDto updatedArticle = service.findById(id);
        ArticleUpdateRequest actual = ArticleUpdateRequest.of(updatedArticle.getId(),
                                                              updatedArticle.getTitle(),
                                                              updatedArticle.getContent());

        assertThat(updatedArticle).usingRecursiveComparison().isNotEqualTo(beforeArticle);
        assertThat(actual).usingRecursiveComparison().isEqualTo(updatedArticle);
        assertThat(updatedArticle.getCreatedAt()).isEqualTo(beforeArticle.getCreatedAt());
        assertThat(updatedArticle.getModifiedAt()).isNotEqualTo(beforeArticle.getModifiedAt());
    }
    
    @DisplayName("게시글 삭제 - 게시글 1개 삭제")
    @Test
    void delete_one() {
        // given
        List<ArticleDto> articleDtos = service.findAll();
        int beforeArticlesSize = articleDtos.size();
        
        long id = articleDtos.get(9).getId();
        
        // when
        service.deleteOne(id);
        
        // then
        assertThrows(ArticleNotFoundException.class,
                    () -> service.findById(id),
                    ExceptionType.ARTICLE_NOT_FOUND.getDescription());
        assertThat(service.findAll().size() + 1).isEqualTo(beforeArticlesSize);
    }
    
    @DisplayName("게시글 삭제 - 게시글 여러개 삭제")
    @Test
    void delete_in_non_existent_id() {
        // given
        List<ArticleDto> articleDtos = service.findAll();
        int beforeArticlesSize = articleDtos.size();
        
        Set<Long> ids = Set.of(
                articleDtos.get(1).getId(),
                articleDtos.get(3).getId(),
                articleDtos.get(6).getId());
        
        // when
        service.deleteMany(ids);
        
        // then
        ids.forEach(id -> assertThat(repository.existsById(id)).isFalse());
        assertThat(service.findAll().size() + ids.size()).isEqualTo(beforeArticlesSize);
    }
    
    @BeforeEach
    public void insertDummyData() {
        List<Article> dummyInsert = new ArrayList<>();
        IntStream.rangeClosed(1, 10)
                .forEach(idx -> dummyInsert.add(
                        Article.of(null, "title " + idx, "content " + idx, null, null)));
        
        System.out.println("💁------- dummy data inserted ------------------------------------------------------------------------------------------------------┐");
        repository.saveAll(dummyInsert);
        System.out.println("💁----------------------------------------------------------------------------------------------------------------------------------┘");
        printAllData();
    }
    
    private void printAllData() {
        System.out.println("💁------- show all article data ------------------------------------------------------------------------------------------------------┐");
        repository.findAll()
                .forEach(System.out::println);
        System.out.println("💁------------------------------------------------------------------------------------------------------------------------------------┘");
    }
}