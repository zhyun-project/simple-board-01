package kim.zhyun.board.service.impl;

import kim.zhyun.board.data.ArticleDto;
import kim.zhyun.board.domain.Article;
import kim.zhyun.board.exception.ArticleNotFoundException;
import kim.zhyun.board.repository.ArticleRepository;
import kim.zhyun.board.type.ExceptionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class ArticleServiceImplTest {
    private final ArticleServiceImpl service;
    private final ArticleRepository repository;
    
    public ArticleServiceImplTest(@Autowired ArticleServiceImpl service,
                                  @Autowired ArticleRepository repository) {
        this.service = service;
        this.repository = repository;
    }
    
    @DisplayName("전체 게시글 조회")
    @Test
    void findAll() {
        // when - then
        assertThat(service.findAll())
                .isEqualTo(repository.findAll().stream()
                        .map(ArticleDto::from)
                        .toList());
    }
    
    @DisplayName("없는 게시글 번호 조회 - ArticleNotFound Exception")
    @Test
    void findByIdWithArticleNotFoundException() {
        // given
        long articleId = 1L;
        
        // when - then
        assertThrows(ArticleNotFoundException.class,
                () -> service.findById(articleId),
                ExceptionType.ARTICLE_NOT_FOUND.getDescription());
    }
    
    @DisplayName("게시글 번호 조회 - 성공")
    @Test
    void findById() {
        // given
        insertDummyData();
        
        long articleId = 1L;
        
        // when - then
        assertThat(service.findById(articleId))
                .usingRecursiveComparison()
                .isEqualTo(ArticleDto.from(repository.findById(articleId).orElseGet(Article::new)));
    }
    
    
    
    private void insertDummyData() {
        List<Article> dummyInsert = new ArrayList<>();
        IntStream.rangeClosed(1, 10)
                .forEach(idx -> dummyInsert.add(Article.builder()
                        .title("title " + idx)
                        .content("content " + idx).build()));
        
        System.out.println("💁------- dummy data inserted ------------------------------------------------------------------------------------------------------┐");
        repository.saveAll(dummyInsert);
        System.out.println("💁----------------------------------------------------------------------------------------------------------------------------------┘");
    }
}