package com.example.thymeleaf.view.controller;

import com.example.thymeleaf.article.entity.Article;
import com.example.thymeleaf.article.service.ArticleService;
import com.example.thymeleaf.view.dto.ArticleListViewResponse;
import com.example.thymeleaf.view.dto.ArticleViewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ArticleViewController {
    private final ArticleService articleService;

    // 목록 반환
    @GetMapping("/articles")
    public String getArticles(Model model){
        List<ArticleListViewResponse> articles = articleService.findAll()
                .stream()
                .map(ArticleListViewResponse::new)
                .toList();

        model.addAttribute("articles",articles);

        return "articleList";
    }

    // 글 반환
    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable long id,Model model){
        Article article = articleService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";
    }

    // 수정
    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model){
        if(id==null){
            model.addAttribute("article", new ArticleViewResponse());
        }else{
            Article article = articleService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }
        return "newArticle";
    }

}
