package kim.zhyun.board.exception;

import kim.zhyun.board.type.ExceptionType;

public class ArticleNotFoundException extends RuntimeException {
    
    public ArticleNotFoundException(ExceptionType type) {
        super(type.getDescription());
    }
    
}
