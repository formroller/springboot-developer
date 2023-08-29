package com.example.thymeleaf.article.service;

import com.example.thymeleaf.article.dto.AddArticleRequest;
import com.example.thymeleaf.article.dto.UpdateArticleRequest;
import com.example.thymeleaf.article.entity.Article;
import com.example.thymeleaf.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    // 저장
    public Article save(AddArticleRequest request){
        return articleRepository.save(request.toEntity());
    }

    // 전체 조회
    public List<Article> findAll(){
        return articleRepository.findAll();
    }

    // 단일 조회
    public Article findById(Long id){
        return  articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not Found"+id));
    }

    // 삭제
    public void delete(long id) {
        articleRepository.deleteById(id);
    }

    // 수정
    @Transactional
    public Article update(long id, UpdateArticleRequest request){
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found : "+id));

        article.update(request.getTitle(), request.getContent());

        return article;
    }

}
