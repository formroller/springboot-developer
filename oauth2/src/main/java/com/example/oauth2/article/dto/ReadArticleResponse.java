package com.example.oauth2.article.dto;

import com.example.oauth2.article.entity.Article;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReadArticleResponse { // ArticleViewResponse

    private final String title;
    private final String content;


    public ReadArticleResponse(Article article){
        this.title = article.getTitle();
        this.content= article.getContent();
    }
}
