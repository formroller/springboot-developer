package com.example.blog.controller;


import com.example.blog.article.dto.AddArticleRequest;
import com.example.blog.article.dto.UpdateArticleRequest;
import com.example.blog.article.entity.Article;
import com.example.blog.article.repository.ArticleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ArticleApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    ArticleRepository articleRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc= MockMvcBuilders.webAppContextSetup(context).build();
        articleRepository.deleteAll();
    }


    @DisplayName("addArticle : 글 추가")
    @Test
    public void addArticle() throws Exception{
        // given
        final String url = "/api/articles";
        final String title = "title";
        final String content= "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        // 객체 Json으로 직렬화
        final String requestBody = objectMapper.writeValueAsString(userRequest);


        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then

        result.andExpect(status().isCreated());

        List<Article> articles = articleRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("findAllArticles : 글 목록 조회")
    @Test
    public void findAllArticles() throws Exception{
        // given 글 저장
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        articleRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());


        // when 조회 api 호출
        final ResultActions result = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));


        // then 반환 값 200, 0번째 요소의 content, title 일치 여부 확인
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].title").value(title));
    }

    @DisplayName("findById : 단일 글 조회")
    @Test
    public void findArticle() throws Exception{
        // given 블로그 저장
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = articleRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // when id 값으로 api 호출
        ResultActions result = mockMvc.perform(get(url, savedArticle.getId()));

        // then code-200, content/title 같은지 확인
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));
    }

    @DisplayName("deleteArticle : 글 삭제")
    public void deleteArticle() throws Exception {
        // given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = articleRepository.save(
                Article.builder()
                        .title(title)
                        .content(content)
                        .build());

        // when
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isNoContent());
        // then
        List<Article> articles = articleRepository.findAll();
        assertThat(articles).isEmpty();;
    }

    @DisplayName("updateArticle : 글 수정")
    @Test
    public void updateArticle() throws Exception {
        // given - 수정에 필요한 객체 생성
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = articleRepository.save(
                Article.builder()
                        .title(title)
                        .content(content)
                        .build());

        final String newTitle = "new title";
        final String newContent = "new content";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        // when - 수정 요청 발신
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(request)));

        // then - 응답 코드(200) 확인 및 id 조회 통해 수정 확인
        result.andExpect(status().isOk());

        Article article = articleRepository.findById(savedArticle.getId()).get();

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }
}
