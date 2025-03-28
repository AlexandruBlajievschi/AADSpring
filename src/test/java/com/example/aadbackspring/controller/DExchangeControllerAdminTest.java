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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"admin"})
public class DExchangeControllerAdminTest {

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

        // Save the sample exchange.
        sampleExchange = repository.save(sampleExchange);
    }

    // ----- CREATE -----
    @Test
    public void testCreateDExchange_AsAdmin_Success() throws Exception {
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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("NewDEX")))
                .andExpect(jsonPath("$.slug", is("new-dex")));
    }

    // ----- READ ALL -----
    @Test
    public void testGetAllDExchanges_AsAdmin_Success() throws Exception {
        mockMvc.perform(get("/dexchanges"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    // ----- READ BY ID -----
    @Test
    public void testGetDExchangeById_AsAdmin_Success() throws Exception {
        mockMvc.perform(get("/dexchanges/{id}", sampleExchange.getId()))
                .andExpect(status().isOk())
                // The internal DB id is hidden; the externalId is mapped to "id" in JSON.
                .andExpect(jsonPath("$.id", is(123)))
                .andExpect(jsonPath("$.name", is("SampleDEX")));
    }

    @Test
    public void testGetDExchangeById_AsAdmin_NotFound() throws Exception {
        mockMvc.perform(get("/dexchanges/{id}", 9999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("DExchange not found with id 9999")));
    }

    // ----- UPDATE -----
    @Test
    public void testUpdateDExchange_AsAdmin_Success() throws Exception {
        // Update some fields.
        sampleExchange.setName("UpdatedDEX");
        sampleExchange.setSlug("updated-dex");
        sampleExchange.setNumMarketPairs("15");

        mockMvc.perform(put("/dexchanges/{id}", sampleExchange.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleExchange)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("UpdatedDEX")))
                .andExpect(jsonPath("$.slug", is("updated-dex")))
                .andExpect(jsonPath("$.num_market_pairs", is("15")));
    }

    @Test
    public void testUpdateDExchange_AsAdmin_NotFound() throws Exception {
        DExchange updateData = new DExchange();
        updateData.setExternalId(111L);
        updateData.setNumMarketPairs("5");
        updateData.setLastUpdated(OffsetDateTime.now());
        updateData.setMarketShare(0.3);
        updateData.setType("dex");
        updateData.setQuote(Collections.emptyList());
        updateData.setName("NonExistentDEX");
        updateData.setSlug("nonexistent-dex");
        updateData.setStatus("inactive");

        mockMvc.perform(put("/dexchanges/{id}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("DExchange not found with id 9999")));
    }

    // ----- DELETE -----
    @Test
    public void testDeleteDExchange_AsAdmin_Success() throws Exception {
        mockMvc.perform(delete("/dexchanges/{id}", sampleExchange.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("DExchange deleted successfully")));

        // Verify deletion.
        mockMvc.perform(get("/dexchanges/{id}", sampleExchange.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("DExchange not found with id " + sampleExchange.getId())));
    }

    @Test
    public void testDeleteDExchange_AsAdmin_NotFound() throws Exception {
        mockMvc.perform(delete("/dexchanges/{id}", 9999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("DExchange not found with id 9999")));
    }
}
