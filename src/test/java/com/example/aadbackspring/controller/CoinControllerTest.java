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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CoinControllerTest {

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

    // ---- CREATE COIN (Happy Path) ----
    @Test
    public void testCreateCoin_Success() throws Exception {
        Coin newCoin = new Coin();
        newCoin.setExternalId(101L);
        newCoin.setName("Ethereum");
        newCoin.setSymbol("ETH");
        newCoin.setPrice(4000.0);
        newCoin.setPercentChange24h(1.2);

        mockMvc.perform(post("/coins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCoin)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Ethereum")))
                .andExpect(jsonPath("$.symbol", is("ETH")))
                .andExpect(jsonPath("$.price", is(4000.0)))
                .andExpect(jsonPath("$.percent_change_24h", is(1.2)));
    }

    // ---- GET ALL COINS (Happy Path) ----
    @Test
    public void testGetAllCoins_Success() throws Exception {
        mockMvc.perform(get("/coins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    // ---- GET COIN BY ID (Happy Path) ----
    @Test
    public void testGetCoinById_Success() throws Exception {
        mockMvc.perform(get("/coins/{id}", sampleCoin.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Bitcoin")))
                .andExpect(jsonPath("$.symbol", is("BTC")));
    }

    // ---- GET COIN BY ID (Not Found) ----
    @Test
    public void testGetCoinById_NotFound() throws Exception {
        mockMvc.perform(get("/coins/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Coin not found")));
    }

    // ---- UPDATE COIN (Happy Path) ----
    @Test
    public void testUpdateCoin_Success() throws Exception {
        sampleCoin.setName("Bitcoin Updated");
        sampleCoin.setSymbol("BTCU");
        sampleCoin.setPrice(51000.0);
        sampleCoin.setPercentChange24h(3.0);

        mockMvc.perform(put("/coins/{id}", sampleCoin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCoin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Bitcoin Updated")))
                .andExpect(jsonPath("$.symbol", is("BTCU")))
                .andExpect(jsonPath("$.price", is(51000.0)))
                .andExpect(jsonPath("$.percent_change_24h", is(3.0)));
    }

    // ---- UPDATE COIN (Not Found) ----
    @Test
    public void testUpdateCoin_NotFound() throws Exception {
        Coin updateCoin = new Coin();
        updateCoin.setExternalId(102L);
        updateCoin.setName("Nonexistent Coin");
        updateCoin.setSymbol("NON");
        updateCoin.setPrice(0.0);
        updateCoin.setPercentChange24h(0.0);

        mockMvc.perform(put("/coins/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCoin)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Coin not found")));
    }

    // ---- DELETE COIN (Happy Path) ----
    @Test
    public void testDeleteCoin_Success() throws Exception {
        mockMvc.perform(delete("/coins/{id}", sampleCoin.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Coin deleted successfully")));

        // Verify deletion by attempting to get the deleted record.
        mockMvc.perform(get("/coins/{id}", sampleCoin.getId()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Coin not found")));
    }

    // ---- DELETE COIN (Not Found) ----
    @Test
    public void testDeleteCoin_NotFound() throws Exception {
        mockMvc.perform(delete("/coins/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Coin not found")));
    }
}
