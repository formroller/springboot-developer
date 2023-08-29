package com.example.blog.article.dto;

import com.example.blog.article.entity.Article;
import lombok.Getter;

@Getter
public class ReadArticleResponse {
    private final String title;
    private final String content;

    public ReadArticleResponse(Article article){
        this.title = article.getTitle();
        this.content= article.getContent();
    }
}
