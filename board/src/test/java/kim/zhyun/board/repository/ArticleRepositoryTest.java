package kim.zhyun.board.repository;

import jakarta.persistence.EntityListeners;
import kim.zhyun.board.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DB Test")
@EntityListeners(AuditingEntityListener.class)
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
        
        // log
        printAllData();
    }
    
    @Test
    @DisplayName("게시글 등록 테스트 - 게시글 1개")
    public void insert_and_read_article_all() {
        // given
        Article article = Article.builder()
                .title("title 1")
                .content("content 1").build();
        
        // when
        Article saved = repository.save(article);
        
        //then
        List<Article> articles = repository.findAll();
        
        assertThat(articles).isEqualTo(List.of(saved));
        assertThat(articles.size()).isEqualTo(1);
        
        // log
        printAllData();
    }
    
    @Test
    @DisplayName("게시글 수정 테스트 - 게시글 1번 업데이트")
    public void update_1L_and_read_article() {
        // given
        insertDummyData();
        
        Article read1L = repository.getReferenceById(1L);

        // when
        Article updated = repository.save(Article.builder()
                .id(read1L.getId())
                .title(read1L.getTitle() + " update 고고")
                .content(read1L.getContent() + " update 고고")
                .createdAt(read1L.getCreatedAt()).build());

        //then
        assertThat(repository.getReferenceById(1L)).isEqualTo(updated);
        
        // log
        printAllData();
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
        
        // log
        printAllData();
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
        
        // log
        printAllData();
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
        
        // log
        printAllData();
    }
    
    @Test
    @DisplayName("게시글 여러개 삭제 - 존재하지 않는 게시글")
    public void delete_none_existent_article_so_many() {
        // given
        insertDummyData();
        
        // when
        Set<Long> articleIds = Set.of(300L, 100L, 800L, 500L);
        
        repository.deleteAllByIdInBatch(articleIds);
        
        // then
        articleIds.forEach(id ->
                assertThat(repository.existsById(id)).isFalse());
        
        // log
        printAllData();
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
        
        // log
        printAllData();
    }
    
    private void printAllData() {
        System.out.println("💁------- show all article data ------------------------------------------------------------------------------------------------------┐");
        repository.findAll()
                .forEach(System.out::println);
        System.out.println("💁------------------------------------------------------------------------------------------------------------------------------------┘");
    }
    
}
