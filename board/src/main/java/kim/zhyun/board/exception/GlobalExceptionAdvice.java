package kim.zhyun.board.exception;

import kim.zhyun.board.data.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    
    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> articleNotFoundException(ArticleNotFoundException e) {
        return ResponseEntity
                .badRequest().body(ApiResponse.<Void>builder()
                        .status(false)
                        .message(e.getMessage()).build());
    }
    
}
