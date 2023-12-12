package kim.zhyun.board.controller;

import kim.zhyun.board.data.ApiResponse;
import kim.zhyun.board.data.ArticleDto;
import kim.zhyun.board.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ArticleController {
    private final ArticleService service;
    
    /**
     * 게시글 조회
     */
    @GetMapping("/articles")
    public ResponseEntity<Object> findAll() {
        return ResponseEntity.ok(ApiResponse.<List<ArticleDto>>builder()
                .status(true)
                .message("article 전체 조회")
                .result(service.findAll()).build());
    }
    
    @GetMapping("/articles/{id}")
    public ResponseEntity<Object> findAll(@PathVariable long id) {
        return ResponseEntity.ok(ApiResponse.<ArticleDto>builder()
                .status(true)
                .message("article " + id + " 조회")
                .result(service.findById(id)).build());
    }
    
    /**
     * 게시글 등록
     */
}
