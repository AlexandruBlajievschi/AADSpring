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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "regularUser", roles = {"user"})
public class TermControllerUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TermRepository termRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Term sampleTerm;

    @BeforeEach
    public void setup() {
        termRepository.deleteAll();

        sampleTerm = new Term();
        sampleTerm.setTerm("Blockchain");
        sampleTerm.setMeaning("A decentralized ledger technology.");
        termRepository.save(sampleTerm);
    }

    // ---- CREATE (User Forbidden) ----
    @Test
    public void testCreateTerm_AsUser_Forbidden() throws Exception {
        Term newTerm = new Term();
        newTerm.setTerm("Cryptocurrency");
        newTerm.setMeaning("Digital currency secured by cryptography.");

        mockMvc.perform(post("/terms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTerm)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error", is("Access Denied: You do not have permission to perform this action. Please contact the administrator if you believe this is an error.")));
    }

    // ---- GET ALL (Accessible to everyone) ----
    @Test
    public void testGetAllTerms_AsUser_Success() throws Exception {
        mockMvc.perform(get("/terms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // ---- GET BY ID (Accessible to everyone) ----
    @Test
    public void testGetTermById_AsUser_Success() throws Exception {
        mockMvc.perform(get("/terms/{id}", sampleTerm.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(sampleTerm.getId().intValue())))
                .andExpect(jsonPath("$.term", is("Blockchain")))
                .andExpect(jsonPath("$.meaning", is("A decentralized ledger technology.")));
    }

    @Test
    public void testGetTermById_AsUser_NotFound() throws Exception {
        mockMvc.perform(get("/terms/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Term not found")));
    }

    // ---- UPDATE (User Forbidden) ----
    @Test
    public void testUpdateTerm_AsUser_Forbidden() throws Exception {
        Term updateData = new Term();
        updateData.setTerm("Blockchain Technology");
        updateData.setMeaning("Updated meaning for blockchain.");

        mockMvc.perform(put("/terms/{id}", sampleTerm.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error", is("Access Denied: You do not have permission to perform this action. Please contact the administrator if you believe this is an error.")));
    }

    @Test
    public void testUpdateTerm_AsUser_NotFound() throws Exception {
        Term updateData = new Term();
        updateData.setTerm("Nonexistent Term");
        updateData.setMeaning("No meaning.");

        mockMvc.perform(put("/terms/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isForbidden()); // Even if term isn’t found, the user’s role blocks access
    }

    // ---- DELETE (User Forbidden) ----
    @Test
    public void testDeleteTerm_AsUser_Forbidden() throws Exception {
        mockMvc.perform(delete("/terms/{id}", sampleTerm.getId()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error", is("Access Denied: You do not have permission to perform this action. Please contact the administrator if you believe this is an error.")));
    }

    @Test
    public void testDeleteTerm_AsUser_NotFound() throws Exception {
        mockMvc.perform(delete("/terms/999"))
                .andExpect(status().isForbidden());
    }
}
