package com.example.oauth2.article.service;

import com.example.oauth2.article.dto.AddArticleRequest;
import com.example.oauth2.article.dto.UpdateArticleRequest;
import com.example.oauth2.article.entity.Article;
import com.example.oauth2.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    // 저장
    public Article save(AddArticleRequest request, String userName){
        return articleRepository.save(request.toEntity(userName));
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
    public void delete(Long id) {

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found : "+id));

        authorizedArticleAuthor(article);
        articleRepository.delete(article);
    }

    // 수정
    @Transactional
    public Article update(Long id, UpdateArticleRequest request){
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found : "+id));

        article.update(request.getTitle(), request.getContent());

        return article;
    }


    /*유효성 확인*/
    // 게시글 작성한 유저인지 확인
    private static void authorizedArticleAuthor(Article article){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!article.getAuthor().equals(userName)){
            throw new IllegalArgumentException("Not Authorized");
        }
    }

}
