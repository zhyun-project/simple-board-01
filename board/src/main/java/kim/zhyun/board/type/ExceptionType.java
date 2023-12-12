package kim.zhyun.board.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionType {
    ARTICLE_NOT_FOUND("잘못된 게시글 번호입니다."),
    ;
    
    private final String description;
}
