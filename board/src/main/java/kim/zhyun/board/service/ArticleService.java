package kim.zhyun.board.service;

import kim.zhyun.board.data.ArticleDto;

import java.util.List;

public interface ArticleService {
    
    List<ArticleDto> findAll();
    ArticleDto findById(long id);
    
}
