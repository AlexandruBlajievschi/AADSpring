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
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ArticleControllerTest {

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
        // Clear existing data.
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

    // ---- GET ALL ARTICLES ----
    @Test
    public void testGetAllArticles_Success() throws Exception {
        mockMvc.perform(get("/articles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    // ---- GET ARTICLE BY ID ----
    @Test
    public void testGetArticleById_Success() throws Exception {
        mockMvc.perform(get("/articles/{id}", sampleArticle.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(sampleArticle.getId().intValue())))
                .andExpect(jsonPath("$.title", is("Introduction to Altcoins")));
    }

    // ---- CREATE ARTICLE ----
    @Test
    public void testCreateArticle_Success() throws Exception {
        Article newArticle = new Article();
        newArticle.setTitle("Advanced Altcoins");
        newArticle.setContent("This article dives deeper into altcoins.");
        newArticle.setCategory(sampleCategory);

        mockMvc.perform(post("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newArticle)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Advanced Altcoins")))
                .andExpect(jsonPath("$.content", is("This article dives deeper into altcoins.")))
                .andExpect(jsonPath("$.category.id", is(sampleCategory.getId().intValue())));
    }

    // ---- UPDATE ARTICLE ----
    @Test
    public void testUpdateArticle_Success() throws Exception {
        Article updateData = new Article();
        updateData.setTitle("Updated Altcoins Introduction");
        updateData.setContent("Updated content for the altcoins article.");
        updateData.setCategory(sampleCategory);

        mockMvc.perform(put("/articles/{id}", sampleArticle.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Altcoins Introduction")))
                .andExpect(jsonPath("$.content", is("Updated content for the altcoins article.")))
                .andExpect(jsonPath("$.category.id", is(sampleCategory.getId().intValue())));
    }

    // ---- DELETE ARTICLE ----
    @Test
    public void testDeleteArticle_Success() throws Exception {
        mockMvc.perform(delete("/articles/{id}", sampleArticle.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("deleted")));

        // Verify deletion.
        mockMvc.perform(get("/articles/{id}", sampleArticle.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}