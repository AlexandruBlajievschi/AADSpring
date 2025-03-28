package com.example.aadbackspring.controller;

import com.example.aadbackspring.model.DExchange;
import com.example.aadbackspring.repository.DExchangeRepository;
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

import java.time.OffsetDateTime;
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
public class DExchangeControllerUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DExchangeRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private DExchange sampleExchange;

    @BeforeEach
    public void setup() {
        // Clear repository before each test.
        repository.deleteAll();

        // Create a sample DExchange record.
        sampleExchange = new DExchange();
        sampleExchange.setExternalId(123L);
        sampleExchange.setNumMarketPairs("10");
        sampleExchange.setLastUpdated(OffsetDateTime.now());
        sampleExchange.setMarketShare(0.5);
        sampleExchange.setType("dex");
        sampleExchange.setQuote(Collections.emptyList());
        sampleExchange.setName("SampleDEX");
        sampleExchange.setSlug("sample-dex");
        sampleExchange.setStatus("active");

        sampleExchange = repository.save(sampleExchange);
    }

    // GET endpoints should be allowed for a regular user.
    @Test
    public void testGetAllDExchanges_AsUser_Success() throws Exception {
        mockMvc.perform(get("/dexchanges"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    public void testGetDExchangeById_AsUser_Success() throws Exception {
        mockMvc.perform(get("/dexchanges/{id}", sampleExchange.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(123)))
                .andExpect(jsonPath("$.name", is("SampleDEX")));
    }

    // POST, PUT, DELETE endpoints should be forbidden for regular users.
    @Test
    public void testCreateDExchange_AsUser_Forbidden() throws Exception {
        DExchange newExchange = new DExchange();
        newExchange.setExternalId(456L);
        newExchange.setNumMarketPairs("20");
        newExchange.setLastUpdated(OffsetDateTime.now());
        newExchange.setMarketShare(0.8);
        newExchange.setType("dex");
        newExchange.setQuote(Collections.emptyList());
        newExchange.setName("NewDEX");
        newExchange.setSlug("new-dex");
        newExchange.setStatus("active");

        mockMvc.perform(post("/dexchanges")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newExchange)))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Access Denied")));
    }

    @Test
    public void testUpdateDExchange_AsUser_Forbidden() throws Exception {
        sampleExchange.setName("UpdatedDEX");
        sampleExchange.setSlug("updated-dex");
        sampleExchange.setNumMarketPairs("15");

        mockMvc.perform(put("/dexchanges/{id}", sampleExchange.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleExchange)))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Access Denied")));
    }

    @Test
    public void testDeleteDExchange_AsUser_Forbidden() throws Exception {
        mockMvc.perform(delete("/dexchanges/{id}", sampleExchange.getId()))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Access Denied")));
    }
}
