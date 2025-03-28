package com.example.aadbackspring.controller;

import com.example.aadbackspring.model.Coin;
import com.example.aadbackspring.repository.CoinRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "regularuser", roles = {"user"})
public class CoinControllerUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Coin sampleCoin;

    @BeforeEach
    public void setup() {
        // Clear repository before each test.
        coinRepository.deleteAll();

        // Create and save a sample coin.
        sampleCoin = new Coin();
        sampleCoin.setExternalId(100L);
        sampleCoin.setName("Bitcoin");
        sampleCoin.setSymbol("BTC");
        sampleCoin.setPrice(50000.0);
        sampleCoin.setPercentChange24h(2.5);
        sampleCoin = coinRepository.save(sampleCoin);
    }

    // ---- READ ENDPOINTS (Allowed for User) ----

    @Test
    public void testGetAllCoins_AsUser_Success() throws Exception {
        mockMvc.perform(get("/coins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    public void testGetCoinById_AsUser_Success() throws Exception {
        mockMvc.perform(get("/coins/{id}", sampleCoin.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.name", is("Bitcoin")))
                .andExpect(jsonPath("$.symbol", is("BTC")));
    }

    // ---- PROTECTED ENDPOINTS (User Forbidden) ----

    @Test
    public void testCreateCoin_AsUser_Forbidden() throws Exception {
        Coin newCoin = new Coin();
        newCoin.setExternalId(101L);
        newCoin.setName("Ethereum");
        newCoin.setSymbol("ETH");
        newCoin.setPrice(4000.0);
        newCoin.setPercentChange24h(1.2);

        mockMvc.perform(post("/coins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCoin)))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Access Denied")));
    }

    @Test
    public void testUpdateCoin_AsUser_Forbidden() throws Exception {
        sampleCoin.setName("Bitcoin Updated");
        sampleCoin.setSymbol("BTCU");
        sampleCoin.setPrice(51000.0);
        sampleCoin.setPercentChange24h(3.0);

        mockMvc.perform(put("/coins/{id}", sampleCoin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCoin)))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Access Denied")));
    }

    @Test
    public void testDeleteCoin_AsUser_Forbidden() throws Exception {
        mockMvc.perform(delete("/coins/{id}", sampleCoin.getId()))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Access Denied")));
    }
}
