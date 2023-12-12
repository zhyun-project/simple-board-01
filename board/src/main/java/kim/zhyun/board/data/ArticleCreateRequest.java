package kim.zhyun.board.data;

import jakarta.validation.constraints.NotEmpty;
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
    
    @NotEmpty(message = "제목을 입력해주세요")
    private String title;
    
    @NotEmpty(message = "내용을 입력해주세요")
    private String content;
    
    public static Article to(ArticleCreateRequest request) {
        return Article.builder()
                .title(request.getTitle())
                .content(request.getContent()).build();
    }
    
}
