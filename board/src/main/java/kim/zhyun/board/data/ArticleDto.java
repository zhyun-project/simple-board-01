package kim.zhyun.board.data;

import kim.zhyun.board.domain.Article;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter @Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class ArticleDto {
    
    private Long id;
    
    private String title;
    private String content;
    
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    
    public static ArticleDto from(Article source) {
        return of(source.getId(), source.getTitle(), source.getContent(), source.getCreatedAt(), source.getModifiedAt());
    }
    
}
