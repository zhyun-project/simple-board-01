package kim.zhyun.board.data;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@Getter
@JsonInclude(NON_NULL)
public class ApiResponse <T> {
    
    private boolean status;
    private String message;
    private T result;
    
}
