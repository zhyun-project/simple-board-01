package kim.zhyun.board.repository;

import kim.zhyun.board.config.JpaAuditingConfig;
import kim.zhyun.board.domain.Article;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DB Test")
@Import(JpaAuditingConfig.class)
@DataJpaTest
class ArticleRepositoryTest {
    
    private final ArticleRepository repository;
    
    public ArticleRepositoryTest(@Autowired ArticleRepository repository) {
        this.repository = repository;
    }
    
    @Test
    @DisplayName("게시글 조회 테스트 - 게시글 없음")
    public void read_article_all_but_table_is_empty() {
        // when
        List<Article> articles = repository.findAll();
        
        // then
        assertThat(articles).isEqualTo(List.of());
        assertThat(articles.size()).isEqualTo(0);
    }
    
    @Test
    @DisplayName("게시글 등록 테스트 - 게시글 1개")
    public void insert_and_read_article_all() {
        // given
        Article article = Article.of(null, "title 1", "content 1", null, null);
        
        // when
        Article saved = repository.save(article);
        
        //then
        List<Article> articles = repository.findAll();
        
        assertThat(articles).isEqualTo(List.of(saved));
        assertThat(articles.size()).isEqualTo(1);
    }
    
    @Test
    @DisplayName("게시글 수정 테스트 - 게시글 1번 업데이트")
    public void update_1L_and_read_article() {
        // given
        insertDummyData();
        
        Article read1L = repository.getReferenceById(1L);

        // when
        read1L.setTitle("title update 고고");
        read1L.setTitle("content update 고고");
        
        //then
        assertThat(repository.getReferenceById(1L)).isEqualTo(read1L);
    }
    
    @Test
    @DisplayName("게시글 1개 삭제 - 존재하는 게시글")
    public void delete_article_one() {
        // given
        insertDummyData();
        
        // when
        repository.deleteById(3L);
        
        // then
        assertThat(repository.findById(3L)).isEmpty();
    }
    
    @Test
    @DisplayName("게시글 1개 삭제 - 존재하지 않는 게시글")
    public void delete_none_existent_article_one() {
        // given
        insertDummyData();
        
        // when
        repository.deleteById(300L);
        
        // then
        assertThat(repository.findById(300L)).isEmpty();
    }
    
    @Test
    @DisplayName("게시글 여러개 삭제 - 존재하는 게시글")
    public void delete_article_many() {
        // given
        insertDummyData();
        
        // when
        Set<Long> articleIds = Set.of(3L, 1L, 8L, 5L);
        
        repository.deleteAllByIdInBatch(articleIds);
        
        // then
        articleIds.forEach(id ->
                assertThat(repository.existsById(id)).isFalse());
    }
    
    @Test
    @DisplayName("게시글 여러개 삭제 - 존재하지 않는 게시글")
    public void delete_none_existent_article_so_many() {
        // given
        insertDummyData();
        
        Set<Long> articleIds = Set.of(300L, 100L, 800L, 500L);
        
        // when
        repository.deleteAllByIdInBatch(articleIds);
        
        // then
        articleIds.forEach(id ->
                assertThat(repository.existsById(id)).isFalse());
    }
    
    private void insertDummyData() {
        List<Article> dummyInsert = new ArrayList<>();
        IntStream.rangeClosed(1, 10)
                .forEach(idx -> dummyInsert.add(
                        Article.of(null, "title " + idx, "content " + idx, null, null)));
        
        System.out.println("💁------- dummy data inserted ------------------------------------------------------------------------------------------------------┐");
        repository.saveAll(dummyInsert);
        System.out.println("💁----------------------------------------------------------------------------------------------------------------------------------┘");
        
        // log
        printAllData();
    }
    
    @AfterEach
    public void printAllData() {
        System.out.println("💁------- show all article data ------------------------------------------------------------------------------------------------------┐");
        repository.findAll()
                .forEach(System.out::println);
        System.out.println("💁------------------------------------------------------------------------------------------------------------------------------------┘");
    }
    
}
