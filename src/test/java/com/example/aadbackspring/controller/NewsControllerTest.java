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
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
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
        sampleNews.setType("120");
        sampleNews.setSourceKey("coindesk");
        sampleNews.setName("CoinDesk");
        sampleNews.setImageUrl("https://images.cryptocompare.com/news/default/coindesk.png");
        sampleNews.setUrl("https://www.coindesk.com/arc/outboundfeeds/rss/?outputType=xml");
        sampleNews.setLang("EN");
        sampleNews.setSourceType("RSS");
        sampleNews.setLaunchDate(1367884800L);
        sampleNews.setSortOrder(0);
        sampleNews.setBenchmarkScore(71);
        sampleNews.setStatus("ACTIVE");
        sampleNews.setLastUpdatedTs(1741179871L);
        sampleNews.setCreatedOn(1657730129L);
        sampleNews.setUpdatedOn(1711617897L);
        sampleNews = newsRepository.save(sampleNews);
    }

    // ---- CREATE NEWS ----
    @Test
    public void testCreateNews_Success() throws Exception {
        News newNews = new News();
        newNews.setType("120");
        newNews.setSourceKey("cointelegraph");
        newNews.setName("CoinTelegraph");
        newNews.setImageUrl("https://images.cryptocompare.com/news/default/cointelegraph.png");
        newNews.setUrl("https://cointelegraph.com/rss");
        newNews.setLang("EN");
        newNews.setSourceType("RSS");
        newNews.setLaunchDate(1382227200L);
        newNews.setSortOrder(0);
        newNews.setBenchmarkScore(66);
        newNews.setStatus("ACTIVE");
        newNews.setLastUpdatedTs(1741179871L);
        newNews.setCreatedOn(1657730129L);
        newNews.setUpdatedOn(1711620813L);

        mockMvc.perform(post("/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newNews)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("CoinTelegraph")))
                .andExpect(jsonPath("$.sourceKey", is("cointelegraph")));
    }

    // ---- GET ALL NEWS ----
    @Test
    public void testGetAllNews_Success() throws Exception {
        mockMvc.perform(get("/news"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    // ---- GET NEWS BY ID ----
    @Test
    public void testGetNewsById_Success() throws Exception {
        mockMvc.perform(get("/news/{id}", sampleNews.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(sampleNews.getId().intValue())))
                .andExpect(jsonPath("$.name", is("CoinDesk")));
    }

    @Test
    public void testGetNewsById_NotFound() throws Exception {
        mockMvc.perform(get("/news/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("News not found")));
    }

    // ---- UPDATE NEWS ----
    @Test
    public void testUpdateNews_Success() throws Exception {
        News updateData = new News();
        updateData.setType("120");
        updateData.setSourceKey("coindesk");
        updateData.setName("CoinDesk Updated");
        updateData.setImageUrl("https://images.cryptocompare.com/news/default/coindesk.png");
        updateData.setUrl("https://www.coindesk.com/arc/outboundfeeds/rss/?outputType=xml");
        updateData.setLang("EN");
        updateData.setSourceType("RSS");
        updateData.setLaunchDate(1367884800L);
        updateData.setSortOrder(0);
        updateData.setBenchmarkScore(75);
        updateData.setStatus("ACTIVE");
        updateData.setLastUpdatedTs(1741179871L);
        updateData.setCreatedOn(1657730129L);
        updateData.setUpdatedOn(1711617897L);

        mockMvc.perform(put("/news/{id}", sampleNews.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("CoinDesk Updated")))
                .andExpect(jsonPath("$.benchmarkScore", is(75)));
    }

    @Test
    public void testUpdateNews_NotFound() throws Exception {
        News updateData = new News();
        updateData.setType("120");
        updateData.setSourceKey("nonexistent");
        updateData.setName("Nonexistent News");
        updateData.setImageUrl("https://example.com/image.png");
        updateData.setUrl("https://example.com");
        updateData.setLang("EN");
        updateData.setSourceType("RSS");
        updateData.setLaunchDate(0L);
        updateData.setSortOrder(0);
        updateData.setBenchmarkScore(0);
        updateData.setStatus("INACTIVE");
        updateData.setLastUpdatedTs(0L);
        updateData.setCreatedOn(0L);
        updateData.setUpdatedOn(0L);

        mockMvc.perform(put("/news/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("News not found")));
    }

    // ---- DELETE NEWS ----
    @Test
    public void testDeleteNews_Success() throws Exception {
        mockMvc.perform(delete("/news/{id}", sampleNews.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("deleted")));

        // Verify deletion.
        mockMvc.perform(get("/news/{id}", sampleNews.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("News not found")));
    }

    @Test
    public void testDeleteNews_NotFound() throws Exception {
        mockMvc.perform(delete("/news/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("News not found")));
    }

    // ---- TEST API RESPONSE STRUCTURE ----
    @Test
    public void testGetTestApiResponse_Success() throws Exception {
        mockMvc.perform(get("/news/test-api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Data", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.Err", aMapWithSize(0)));
    }
}
