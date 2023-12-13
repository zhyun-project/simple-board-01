package kim.zhyun.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kim.zhyun.board.data.ApiResponse;
import kim.zhyun.board.data.ArticleCreateRequest;
import kim.zhyun.board.data.ArticleDto;
import kim.zhyun.board.data.ArticleUpdateRequest;
import kim.zhyun.board.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;

@Tag(name = "게시글 API", description = "게시글 등록, 조회, 수정, 삭제 기능")
@RequiredArgsConstructor
@RestController
public class ArticleController {
    private final ArticleService service;
    
    @Operation(summary = "게시글 조회 - 전체")
    @GetMapping("/articles")
    public ResponseEntity<Object> findAll() {
        return ResponseEntity.ok(ApiResponse.<List<ArticleDto>>builder()
                .status(true)
                .message("article 전체 조회")
                .result(service.findAll()).build());
    }
    
    @Operation(summary = "게시글 조회 - 1개")
    @GetMapping("/articles/{id}")
    public ResponseEntity<Object> findAll(@PathVariable long id) {
        return ResponseEntity.ok(ApiResponse.<ArticleDto>builder()
                .status(true)
                .message("article " + id + " 조회")
                .result(service.findById(id)).build());
    }
    
    @Operation(summary = "게시글 등록")
    @Parameter(name = "Request body", description = "제목과 내용을 담은 Json Object")
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
    
    
    @Operation(summary = "게시글 수정")
    @Parameter(name = "Request body", description = "게시글 id, 제목, 내용을 담은 Json Object")
    @PutMapping("/articles/{id}")
    public ResponseEntity<Object> update(@PathVariable long id,
                                         @Valid @RequestBody ArticleUpdateRequest request) {
        service.update(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .build().toUri();
        
        return ResponseEntity
                .created(location)
                .body(ApiResponse.<Void>builder()
                        .status(true)
                        .message("수정되었습니다.").build());
    }
    
    
    @Operation(summary = "게시글 삭제 - 한개")
    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Object> delete(@PathVariable long id) {
        service.deleteOne(id);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/articles")
                .build().toUri();
        
        return ResponseEntity
                .noContent()
                .location(location).build();
    }
    
    @Operation(summary = "게시글 삭제 - 여러개")
    @Parameter(name = "Request body", description = "게시글 id를 담은 정수형 Json Array")
    @DeleteMapping("/articles")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids) {
        service.deleteMany(ids);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .build().toUri();
        
        return ResponseEntity
                .noContent()
                .location(location).build();
    }
}
