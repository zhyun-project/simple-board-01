package kim.zhyun.board.data;

import kim.zhyun.board.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class ArticleUpdateRequest {
    
    private Long id;
    
    private String title;
    private String content;
    
    public static Article to(ArticleUpdateRequest request) {
        return Article.builder()
                .id(request.getId())
                .title(request.getTitle())
                .content(request.getContent()).build();
    }
}
