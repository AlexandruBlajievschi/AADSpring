package com.example.aadbackspring.controller;


import com.example.aadbackspring.model.Article;
import com.example.aadbackspring.model.Category;
import com.example.aadbackspring.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Category sampleCategory;

    @BeforeEach
    public void setup() {
        categoryRepository.deleteAll();

        sampleCategory = new Category();
        sampleCategory.setName("Test Category");
        sampleCategory.setDescription("Category for testing articles.");
        sampleCategory = categoryRepository.save(sampleCategory);
    }

    // ---- CREATE ----
    @Test
    public void testCreateCategory_Success() throws Exception {
        Category newCategory = new Category();
        newCategory.setName("Altcoins");
        newCategory.setDescription("Articles about alternative cryptocurrencies.");

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Altcoins")))
                .andExpect(jsonPath("$.description", is("Articles about alternative cryptocurrencies.")));
    }

    // ---- GET ALL ----
    @Test
    public void testGetAllCategories_Success() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    // ---- GET BY ID ----
    @Test
    public void testGetCategoryById_Success() throws Exception {
        mockMvc.perform(get("/categories/{id}", sampleCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(sampleCategory.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Test Category")));
    }

    @Test
    public void testGetCategoryById_NotFound() throws Exception {
        mockMvc.perform(get("/categories/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Category not found")));
    }

    // ---- UPDATE ----
    @Test
    public void testUpdateCategory_Success() throws Exception {
        Category updateData = new Category();
        updateData.setName("Test Category Updated");
        updateData.setDescription("Updated description for testing.");

        mockMvc.perform(put("/categories/{id}", sampleCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Category Updated")))
                .andExpect(jsonPath("$.description", is("Updated description for testing.")));
    }

    @Test
    public void testUpdateCategory_NotFound() throws Exception {
        Category updateData = new Category();
        updateData.setName("Nonexistent");
        updateData.setDescription("Does not exist.");

        mockMvc.perform(put("/categories/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Category not found")));
    }

    // ---- DELETE ----
    @Test
    public void testDeleteCategory_Success() throws Exception {
        mockMvc.perform(delete("/categories/{id}", sampleCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Category deleted successfully")));

        mockMvc.perform(get("/categories/{id}", sampleCategory.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Category not found")));
    }

    @Test
    public void testDeleteCategory_NotFound() throws Exception {
        mockMvc.perform(delete("/categories/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Category not found")));
    }

    // ---- GET CATEGORY WITH ARTICLES ----
    @Test
    public void testGetCategoryWithArticles_Success() throws Exception {
        // Create an article for this category.
        Article article = new Article();
        article.setTitle("Sample Article");
        article.setContent("Sample content.");
        article.setCategory(sampleCategory);
        // Add the article to the category's articles list and save the category.
        sampleCategory.getArticles().add(article);
        categoryRepository.save(sampleCategory);

        mockMvc.perform(get("/categories/{id}/articles", sampleCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(sampleCategory.getId().intValue())))
                .andExpect(jsonPath("$.articles", hasSize(greaterThanOrEqualTo(1))));
    }
}