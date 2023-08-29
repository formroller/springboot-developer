package com.example.security.article.controller;

import com.example.security.article.dto.AddArticleRequest;
import com.example.security.article.dto.ReadArticleResponse;
import com.example.security.article.dto.UpdateArticleRequest;
import com.example.security.article.entity.Article;
import com.example.security.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ArticleApiController {
    private final ArticleService articleService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request){
        Article savedArticle = articleService.save(request);

        return new ResponseEntity<>(savedArticle, HttpStatus.CREATED);
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

        return new ResponseEntity<>(new ReadArticleResponse(article), HttpStatus.OK);
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
