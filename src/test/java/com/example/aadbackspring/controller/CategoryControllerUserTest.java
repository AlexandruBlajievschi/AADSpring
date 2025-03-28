package com.example.aadbackspring.controller;

import com.example.aadbackspring.model.Category;
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

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "regularuser", roles = {"user"})
public class CategoryControllerUserTest {

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

    // ---- READ ENDPOINTS (Allowed for regular user) ----
    @Test
    public void testGetAllCategories_AsUser_Success() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    public void testGetCategoryById_AsUser_Success() throws Exception {
        mockMvc.perform(get("/categories/{id}", sampleCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(sampleCategory.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Test Category")));
    }

    @Test
    public void testGetCategoryById_AsUser_NotFound() throws Exception {
        mockMvc.perform(get("/categories/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Category not found")));
    }

    @Test
    public void testGetCategoryWithArticles_AsUser_Success() throws Exception {
        // Even if there are no articles, the endpoint should return the category
        mockMvc.perform(get("/categories/{id}/articles", sampleCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(sampleCategory.getId().intValue())));
    }

    // ---- PROTECTED ENDPOINTS (User Forbidden) ----
    @Test
    public void testCreateCategory_AsUser_Forbidden() throws Exception {
        Category newCategory = new Category();
        newCategory.setName("Altcoins");
        newCategory.setDescription("Articles about alternative cryptocurrencies.");

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Access Denied")));
    }

    @Test
    public void testUpdateCategory_AsUser_Forbidden() throws Exception {
        Category updateData = new Category();
        updateData.setName("Updated Category");
        updateData.setDescription("Updated description.");

        mockMvc.perform(put("/categories/{id}", sampleCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Access Denied")));
    }

    @Test
    public void testDeleteCategory_AsUser_Forbidden() throws Exception {
        mockMvc.perform(delete("/categories/{id}", sampleCategory.getId()))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Access Denied")));
    }
}
