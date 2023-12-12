package kim.zhyun.board.service.impl;

import kim.zhyun.board.data.ArticleDto;
import kim.zhyun.board.exception.ArticleNotFoundException;
import kim.zhyun.board.repository.ArticleRepository;
import kim.zhyun.board.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static kim.zhyun.board.type.ExceptionType.ARTICLE_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    
    @Override
    public List<ArticleDto> findAll() {
        return articleRepository
                .findAll().stream()
                .filter(Objects::nonNull)
                .map(ArticleDto::from)
                .toList();
    }
    
    @Override
    public ArticleDto findById(long id) {
        return ArticleDto.from(articleRepository
                .findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(ARTICLE_NOT_FOUND)));
    }
    
    
}
