package kim.zhyun.board.service.impl;

import kim.zhyun.board.data.ArticleCreateRequest;
import kim.zhyun.board.data.ArticleDto;
import kim.zhyun.board.data.ArticleUpdateRequest;
import kim.zhyun.board.domain.Article;
import kim.zhyun.board.exception.ArticleNotFoundException;
import kim.zhyun.board.repository.ArticleRepository;
import kim.zhyun.board.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static kim.zhyun.board.type.ExceptionType.ARTICLE_NOT_FOUND;

@RequiredArgsConstructor
@Transactional
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
    
    @Override
    public long save(ArticleCreateRequest request) {
        Article saved = articleRepository.save(ArticleCreateRequest.to(request));
        
        return saved.getId();
    }
    
    @Override
    public void update(ArticleUpdateRequest request) {
        if (!articleRepository.existsById(request.getId()))
            throw new ArticleNotFoundException(ARTICLE_NOT_FOUND);
        
        articleRepository.save(ArticleUpdateRequest.to(request));
    }
    
    @Override
    public void deleteOne(long id) {
        articleRepository.deleteById(id);
    }
    
    @Override
    public void deleteMany(Set<Long> ids) {
        articleRepository.deleteAllByIdInBatch(ids);
    }
    
}
