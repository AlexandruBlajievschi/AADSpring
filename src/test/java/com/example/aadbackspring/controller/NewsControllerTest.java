package com.example.aadbackspring.controller;

import com.example.aadbackspring.model.News;
import com.example.aadbackspring.repository.NewsRepository;

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
public class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private News sampleNews;

    @BeforeEach
    public void setup() {
        // Clear the repository to start fresh.
        newsRepository.deleteAll();

        // Create and persist a sample News record.
        sampleNews = new News();
        sampleNews.setExternalId("123");
        sampleNews.setGuid("sample-guid");
        sampleNews.setPublishedOn(1741950000L);
        sampleNews.setImageurl("https://example.com/image.png");
        sampleNews.setTitle("Sample News Title");
        sampleNews.setUrl("https://example.com/news");
        sampleNews.setBody("This is a sample news body.");
        sampleNews.setTags("sample,news");
        sampleNews.setLang("EN");
        sampleNews.setCategories("Finance");
        sampleNews.setSource("coinpedia");
        sampleNews = newsRepository.save(sampleNews);
    }

    // ---- CREATE NEWS (Good Path) ----
    @Test
    public void testCreateNews_Success() throws Exception {
        News newNews = new News();
        newNews.setExternalId("456");
        newNews.setGuid("new-guid");
        newNews.setPublishedOn(1741960000L);
        newNews.setImageurl("https://example.com/new-image.png");
        newNews.setTitle("New News Title");
        newNews.setUrl("https://example.com/new-news");
        newNews.setBody("This is the body of the new news.");
        newNews.setTags("new,news");
        newNews.setLang("EN");
        newNews.setCategories("Technology");
        newNews.setSource("cryptopotato");

        mockMvc.perform(post("/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newNews)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("New News Title")))
                .andExpect(jsonPath("$.source", is("cryptopotato")));
    }

    // ---- GET ALL NEWS (Good Path) ----
    @Test
    public void testGetAllNews_Success() throws Exception {
        mockMvc.perform(get("/news"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    // ---- GET NEWS BY ID (Good Path) ----
    @Test
    public void testGetNewsById_Success() throws Exception {
        mockMvc.perform(get("/news/{id}", sampleNews.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Sample News Title")))
                .andExpect(jsonPath("$.source", is("coinpedia")));
    }

    // ---- GET NEWS BY ID (Bad Path) ----
    @Test
    public void testGetNewsById_NotFound() throws Exception {
        mockMvc.perform(get("/news/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("News not found")));
    }

    // ---- UPDATE NEWS (Good Path) ----
    @Test
    public void testUpdateNews_Success() throws Exception {
        News updateData = new News();
        updateData.setExternalId("123-updated");
        updateData.setGuid("sample-guid-updated");
        updateData.setPublishedOn(1741951111L);
        updateData.setImageurl("https://example.com/updated-image.png");
        updateData.setTitle("Sample News Title Updated");
        updateData.setUrl("https://example.com/updated-news");
        updateData.setBody("Updated news body.");
        updateData.setTags("updated,news");
        updateData.setLang("EN");
        updateData.setCategories("Economy");
        updateData.setSource("cointelegraph");

        mockMvc.perform(put("/news/{id}", sampleNews.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Sample News Title Updated")))
                .andExpect(jsonPath("$.source", is("cointelegraph")));
    }

    // ---- UPDATE NEWS (Bad Path) ----
    @Test
    public void testUpdateNews_NotFound() throws Exception {
        News updateData = new News();
        updateData.setExternalId("nonexistent");
        updateData.setGuid("nonexistent-guid");
        updateData.setPublishedOn(0L);
        updateData.setImageurl("https://example.com/none.png");
        updateData.setTitle("Nonexistent News");
        updateData.setUrl("https://example.com/none");
        updateData.setBody("No body");
        updateData.setTags("none");
        updateData.setLang("EN");
        updateData.setCategories("None");
        updateData.setSource("none");

        mockMvc.perform(put("/news/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("News not found")));
    }

    // ---- DELETE NEWS (Good Path) ----
    @Test
    public void testDeleteNews_Success() throws Exception {
        mockMvc.perform(delete("/news/{id}", sampleNews.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("deleted successfully")));

        // Verify deletion by attempting to get the deleted record.
        mockMvc.perform(get("/news/{id}", sampleNews.getId()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("News not found")));
    }

    // ---- DELETE NEWS (Bad Path) ----
    @Test
    public void testDeleteNews_NotFound() throws Exception {
        mockMvc.perform(delete("/news/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("News not found")));
    }
}
