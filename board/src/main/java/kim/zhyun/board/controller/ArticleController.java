package kim.zhyun.board.controller;

import jakarta.validation.Valid;
import kim.zhyun.board.data.ApiResponse;
import kim.zhyun.board.data.ArticleCreateRequest;
import kim.zhyun.board.data.ArticleDto;
import kim.zhyun.board.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    @PostMapping("/article")
    public ResponseEntity<Object> save(@Valid @RequestBody ArticleCreateRequest request) {
        long savedId = service.save(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/articles/{id}")
                .buildAndExpand(savedId)
                .toUri();
        
        return ResponseEntity
                .created(location)
                .body(ApiResponse.<Void>builder()
                        .status(true)
                        .message("등록되었습니다.").build());
    }
}
