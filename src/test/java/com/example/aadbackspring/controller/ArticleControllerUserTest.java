package com.example.aadbackspring.controller;

import com.example.aadbackspring.model.Article;
import com.example.aadbackspring.model.Category;
import com.example.aadbackspring.repository.ArticleRepository;
import com.example.aadbackspring.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "regularuser", roles = {"user"})
public class ArticleControllerUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Category sampleCategory;
    private Article sampleArticle;

    @BeforeEach
    public void setup() {
        // Clear repositories
        articleRepository.deleteAll();
        categoryRepository.deleteAll();

        // Create and persist a sample category.
        sampleCategory = new Category();
        sampleCategory.setName("Test Category");
        sampleCategory.setDescription("Category for testing articles.");
        sampleCategory = categoryRepository.save(sampleCategory);

        // Create and persist a sample article.
        sampleArticle = new Article();
        sampleArticle.setTitle("Introduction to Altcoins");
        sampleArticle.setContent("This article explains altcoins.");
        sampleArticle.setCategory(sampleCategory);
        sampleArticle = articleRepository.save(sampleArticle);
    }

    // ---- READ ENDPOINTS (Allowed for regular user) ----
    @Test
    public void testGetAllArticles_AsUser_Success() throws Exception {
        mockMvc.perform(get("/articles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    public void testGetArticleById_AsUser_Success() throws Exception {
        mockMvc.perform(get("/articles/{id}", sampleArticle.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(sampleArticle.getId().intValue())))
                .andExpect(jsonPath("$.title", is("Introduction to Altcoins")));
    }

    @Test
    public void testGetArticleById_AsUser_NotFound() throws Exception {
        mockMvc.perform(get("/articles/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("Article not found")));
    }

    // ---- PROTECTED ENDPOINTS (Should be Forbidden for regular user) ----
    @Test
    public void testCreateArticle_AsUser_Forbidden() throws Exception {
        Article newArticle = new Article();
        newArticle.setTitle("Advanced Altcoins");
        newArticle.setContent("This article dives deeper into altcoins.");
        newArticle.setCategory(sampleCategory);

        mockMvc.perform(post("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newArticle)))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Access Denied")));
    }

    @Test
    public void testUpdateArticle_AsUser_Forbidden() throws Exception {
        Article updateData = new Article();
        updateData.setTitle("Updated Altcoins Introduction");
        updateData.setContent("Updated content for the altcoins article.");
        updateData.setCategory(sampleCategory);

        mockMvc.perform(put("/articles/{id}", sampleArticle.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Access Denied")));
    }

    @Test
    public void testDeleteArticle_AsUser_Forbidden() throws Exception {
        mockMvc.perform(delete("/articles/{id}", sampleArticle.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Access Denied")));
    }
}
