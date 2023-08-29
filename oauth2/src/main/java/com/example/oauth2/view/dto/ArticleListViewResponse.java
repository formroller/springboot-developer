package com.example.oauth2.view.dto;

import com.example.oauth2.article.entity.Article;
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
