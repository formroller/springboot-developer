package com.example.oauth2.article.controller;

import com.example.oauth2.article.dto.AddArticleRequest;
import com.example.oauth2.article.dto.ReadArticleResponse;
import com.example.oauth2.article.dto.UpdateArticleRequest;
import com.example.oauth2.article.entity.Article;
import com.example.oauth2.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ArticleApiController {
    private final ArticleService articleService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request, Principal principal){ // Principal - 현재 인증 정보 가져오는 객체
        Article savedArticle = articleService.save(request, principal.getName());

        return new ResponseEntity(savedArticle, HttpStatus.CREATED);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ReadArticleResponse>> findAllArticles(){
        List<ReadArticleResponse> articles = articleService.findAll()
                .stream()
                .map(ReadArticleResponse::new)
                .toList();

//        return ResponseEntity.ok()
//                .body(articles);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ReadArticleResponse> findArticle(@PathVariable long id){
        Article article = articleService.findById(id);

        return new ResponseEntity(article, HttpStatus.OK);
    }
    
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id){
        articleService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id,
                                                 @RequestBody UpdateArticleRequest request){
        Article updateArticle = articleService.update(id, request);

        return new ResponseEntity<>(updateArticle, HttpStatus.OK);
    }
}
