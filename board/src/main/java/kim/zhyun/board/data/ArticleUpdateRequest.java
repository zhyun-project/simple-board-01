package kim.zhyun.board.data;

import jakarta.validation.constraints.NotEmpty;
import kim.zhyun.board.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@ToString
@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class ArticleUpdateRequest {
    
    private Long id;
    
    @NotEmpty(message = "제목을 입력해주세요")
    private String title;
    
    @NotEmpty(message = "내용을 입력해주세요")
    private String content;
    
    public static Article to(ArticleUpdateRequest request) {
        return Article.builder()
                .id(request.getId())
                .title(request.getTitle())
                .content(request.getContent()).build();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ArticleUpdateRequest that = (ArticleUpdateRequest) obj;
        
        if (!Objects.equals(this.id, that.getId())) return false;
        if (!Objects.equals(this.title, that.getTitle())) return false;
        return Objects.equals(this.content, that.getContent());
    }
    
}
