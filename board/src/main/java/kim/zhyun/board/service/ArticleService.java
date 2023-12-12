package kim.zhyun.board.service;

import kim.zhyun.board.data.ArticleCreateRequest;
import kim.zhyun.board.data.ArticleDto;
import kim.zhyun.board.data.ArticleUpdateRequest;

import java.util.List;
import java.util.Set;

public interface ArticleService {
    
    List<ArticleDto> findAll();
    ArticleDto findById(long id);
    long save(ArticleCreateRequest request);
    void update(ArticleUpdateRequest request);
    void deleteOne(long id);
    void deleteMany(Set<Long> ids);
    
}
