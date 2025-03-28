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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "regularuser", roles = {"user"})
public class NewsControllerUserTest {

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

    // ---- GET ALL NEWS (Allowed for regular user) ----
    @Test
    public void testGetAllNews_AsUser_Success() throws Exception {
        mockMvc.perform(get("/news"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    // ---- GET NEWS BY ID (Allowed for regular user) ----
    @Test
    public void testGetNewsById_AsUser_Success() throws Exception {
        mockMvc.perform(get("/news/{id}", sampleNews.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Sample News Title")))
                .andExpect(jsonPath("$.source", is("coinpedia")));
    }

    // ---- CREATE NEWS (Forbidden for regular user) ----
    @Test
    public void testCreateNews_AsUser_Forbidden() throws Exception {
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
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Access Denied")));
    }

    // ---- UPDATE NEWS (Forbidden for regular user) ----
    @Test
    public void testUpdateNews_AsUser_Forbidden() throws Exception {
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
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Access Denied")));
    }

    // ---- DELETE NEWS (Forbidden for regular user) ----
    @Test
    public void testDeleteNews_AsUser_Forbidden() throws Exception {
        mockMvc.perform(delete("/news/{id}", sampleNews.getId()))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Access Denied")));
    }
}
