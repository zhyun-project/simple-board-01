package kim.zhyun.board.data;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class ValidExceptionResponse {
    
    private String field;
    private String message;
    
}
