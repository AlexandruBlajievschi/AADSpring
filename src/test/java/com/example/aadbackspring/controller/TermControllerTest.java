package com.example.aadbackspring.controller;


import com.example.aadbackspring.model.Term;
import com.example.aadbackspring.repository.TermRepository;
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
public class TermControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TermRepository termRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Term sampleTerm;

    @BeforeEach
    public void setup() {
        // Clear the repository before each test to ensure a clean state
        termRepository.deleteAll();

        // Create a sample term to use in tests
        sampleTerm = new Term();
        sampleTerm.setTerm("Blockchain");
        sampleTerm.setMeaning("A decentralized ledger technology.");
        termRepository.save(sampleTerm);
    }

    // ---- CREATE ----
    @Test
    public void testCreateTerm_Success() throws Exception {
        Term newTerm = new Term();
        newTerm.setTerm("Cryptocurrency");
        newTerm.setMeaning("Digital currency secured by cryptography.");

        mockMvc.perform(post("/terms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTerm)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.term", is("Cryptocurrency")))
                .andExpect(jsonPath("$.meaning", is("Digital currency secured by cryptography.")));
    }

    // ---- GET ALL ----
    @Test
    public void testGetAllTerms_Success() throws Exception {
        mockMvc.perform(get("/terms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // ---- GET BY ID ----
    @Test
    public void testGetTermById_Success() throws Exception {
        mockMvc.perform(get("/terms/{id}", sampleTerm.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(sampleTerm.getId().intValue())))
                .andExpect(jsonPath("$.term", is("Blockchain")))
                .andExpect(jsonPath("$.meaning", is("A decentralized ledger technology.")));
    }

    @Test
    public void testGetTermById_NotFound() throws Exception {
        mockMvc.perform(get("/terms/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Term not found")));
    }

    // ---- UPDATE ----
    @Test
    public void testUpdateTerm_Success() throws Exception {
        Term updateData = new Term();
        updateData.setTerm("Blockchain Technology");
        updateData.setMeaning("Updated meaning for blockchain.");

        mockMvc.perform(put("/terms/{id}", sampleTerm.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.term", is("Blockchain Technology")))
                .andExpect(jsonPath("$.meaning", is("Updated meaning for blockchain.")));
    }

    @Test
    public void testUpdateTerm_NotFound() throws Exception {
        Term updateData = new Term();
        updateData.setTerm("Nonexistent Term");
        updateData.setMeaning("No meaning.");

        mockMvc.perform(put("/terms/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Term not found")));
    }

    // ---- DELETE ----
    @Test
    public void testDeleteTerm_Success() throws Exception {
        mockMvc.perform(delete("/terms/{id}", sampleTerm.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Term deleted successfully")));

        // Verify that the term is no longer present
        mockMvc.perform(get("/terms/{id}", sampleTerm.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Term not found")));
    }

    @Test
    public void testDeleteTerm_NotFound() throws Exception {
        mockMvc.perform(delete("/terms/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Term not found")));
    }
}