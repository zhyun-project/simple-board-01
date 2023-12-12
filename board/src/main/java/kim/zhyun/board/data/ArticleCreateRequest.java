package kim.zhyun.board.data;

import kim.zhyun.board.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
public class ArticleCreateRequest {
    
    private String title;
    private String content;
    
    public static Article to(ArticleCreateRequest request) {
        return Article.builder()
                .title(request.getTitle())
                .content(request.getContent()).build();
    }
    
}
