package kim.zhyun.board.exception;

import kim.zhyun.board.data.ApiResponse;
import kim.zhyun.board.data.ValidExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<Object> articleNotFoundException(ArticleNotFoundException e) {
        return ResponseEntity
                .badRequest().body(ApiResponse.<Void>builder()
                        .status(false)
                        .message(e.getMessage()).build());
    }
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        List<ValidExceptionResponse> list = new ArrayList<>();
        
        ex.getBindingResult()
                .getAllErrors()
                .forEach(objectError -> {
                    FieldError field = (FieldError) objectError;
                    String message = objectError.getDefaultMessage();
                    
                    list.add(ValidExceptionResponse.builder()
                            .field(field.getField())
                            .message(message).build());
                });
        
        list.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getField(), o2.getField()));
        
        return ResponseEntity
                .badRequest().body(ApiResponse.<List<ValidExceptionResponse>>builder()
                        .status(false)
                        .message("valid error")
                        .result(list).build());
    }
    
}
