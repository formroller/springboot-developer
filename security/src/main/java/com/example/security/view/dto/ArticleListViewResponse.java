package com.example.security.view.dto;

import com.example.security.article.entity.Article;
import lombok.Getter;

@Getter
public class ArticleListViewResponse {
    private long id;
    private String title;
    private String content;

    public ArticleListViewResponse(Article article){
        this.id = article.getId();
        this.title = article.getTitle();;
        this.content = article.getContent();

    }

}
