package com.example.thymeleaf.article.repository;

import com.example.thymeleaf.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
