package com.example.aadbackspring.controller;

import com.example.aadbackspring.model.DExchange;
import com.example.aadbackspring.model.DExchangeQuote;
import com.example.aadbackspring.repository.DExchangeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
public class DExchangeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DExchangeRepository dExchangeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private DExchange sampleExchange;

    @BeforeEach
    public void setup() {
        // Clear the repository to ensure a clean test state.
        dExchangeRepository.deleteAll();

        // Create a sample DExchange entity based on the external API structure.
        sampleExchange = new DExchange();
        sampleExchange.setExternalId(1348L);
        sampleExchange.setNumMarketPairs("939");
        sampleExchange.setLastUpdated(OffsetDateTime.parse("2025-03-05T14:58:35.000Z"));
        sampleExchange.setMarketShare(0.17619097);
        sampleExchange.setType("swap");

        DExchangeQuote quote = new DExchangeQuote();
        quote.setConvertId("2781");
        quote.setMarketType("spot");
        quote.setLastUpdated(OffsetDateTime.parse("2025-03-05T14:58:35.000Z"));
        quote.setVolume24h(1569032860.4631765);
        quote.setPercentChangeVolume24h(-34.55170688);
        quote.setNumTransactions24h(139410L);
        sampleExchange.setQuote(Collections.singletonList(quote));

        sampleExchange.setName("Uniswap v3 (Ethereum)");
        sampleExchange.setSlug("uniswap-v3");
        sampleExchange.setStatus("active");

        sampleExchange = dExchangeRepository.save(sampleExchange);
    }

    // ----- CREATE -----
    @Test
    public void testCreateDExchange_Success() throws Exception {
        DExchange newExchange = new DExchange();
        newExchange.setExternalId(1342L);
        newExchange.setNumMarketPairs("1780");
        newExchange.setLastUpdated(OffsetDateTime.parse("2025-03-05T14:58:15.000Z"));
        newExchange.setMarketShare(0.13106021);
        newExchange.setType("swap");

        DExchangeQuote quote = new DExchangeQuote();
        quote.setConvertId("2781");
        quote.setMarketType("spot");
        quote.setLastUpdated(OffsetDateTime.parse("2025-03-05T14:58:15.000Z"));
        quote.setVolume24h(1167130101.961565);
        quote.setPercentChangeVolume24h(-31.61520385);
        quote.setNumTransactions24h(16363454L);
        newExchange.setQuote(Collections.singletonList(quote));

        newExchange.setName("Raydium");
        newExchange.setSlug("raydium");
        newExchange.setStatus("active");

        mockMvc.perform(post("/dexchanges")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newExchange)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Raydium")))
                .andExpect(jsonPath("$.slug", is("raydium")))
                .andExpect(jsonPath("$.id", is(1342)));
    }

    // ----- GET ALL -----
    @Test
    public void testGetAllDExchanges_Success() throws Exception {
        mockMvc.perform(get("/dexchanges"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    // ----- GET BY ID -----
    @Test
    public void testGetDExchangeById_Success() throws Exception {
        mockMvc.perform(get("/dexchanges/{id}", sampleExchange.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Uniswap v3 (Ethereum)")))
                .andExpect(jsonPath("$.id", is(1348)));
    }

    @Test
    public void testGetDExchangeById_NotFound() throws Exception {
        mockMvc.perform(get("/dexchanges/{id}", 99999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("DExchange not found")));
    }

    // ----- UPDATE -----
    @Test
    public void testUpdateDExchange_Success() throws Exception {
        DExchange updateData = new DExchange();
        updateData.setExternalId(1348L);
        updateData.setNumMarketPairs("940");
        updateData.setLastUpdated(OffsetDateTime.parse("2025-03-05T15:00:00.000Z"));
        updateData.setMarketShare(0.18000000);
        updateData.setType("swap");

        DExchangeQuote newQuote = new DExchangeQuote();
        newQuote.setConvertId("2781");
        newQuote.setMarketType("spot");
        newQuote.setLastUpdated(OffsetDateTime.parse("2025-03-05T15:00:00.000Z"));
        newQuote.setVolume24h(1600000000.0);
        newQuote.setPercentChangeVolume24h(-30.0);
        newQuote.setNumTransactions24h(140000L);
        updateData.setQuote(Collections.singletonList(newQuote));

        updateData.setName("Uniswap v3 (Ethereum) Updated");
        updateData.setSlug("uniswap-v3-updated");
        updateData.setStatus("active");

        mockMvc.perform(put("/dexchanges/{id}", sampleExchange.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Uniswap v3 (Ethereum) Updated")))
                .andExpect(jsonPath("$.num_market_pairs", is("940")));
    }

    @Test
    public void testUpdateDExchange_NotFound() throws Exception {
        DExchange updateData = new DExchange();
        updateData.setExternalId(9999L);
        updateData.setNumMarketPairs("0");
        updateData.setLastUpdated(OffsetDateTime.now());
        updateData.setMarketShare(0.0);
        updateData.setType("swap");
        updateData.setQuote(Collections.emptyList());
        updateData.setName("Nonexistent");
        updateData.setSlug("nonexistent");
        updateData.setStatus("inactive");

        mockMvc.perform(put("/dexchanges/{id}", 99999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("DExchange not found")));
    }

    // ----- DELETE -----
    @Test
    public void testDeleteDExchange_Success() throws Exception {
        mockMvc.perform(delete("/dexchanges/{id}", sampleExchange.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("deleted successfully")));

        // Verify deletion by attempting to get the deleted entity.
        mockMvc.perform(get("/dexchanges/{id}", sampleExchange.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteDExchange_NotFound() throws Exception {
        mockMvc.perform(delete("/dexchanges/{id}", 99999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("DExchange not found")));
    }

    // ----- TEST API RESPONSE -----
    @Test
    public void testGetTestApiResponse_Success() throws Exception {
        mockMvc.perform(get("/dexchanges/test-api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.status", aMapWithSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.status.error_code", is("0")));
    }
}
