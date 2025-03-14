package com.example.aadbackspring.service;

import com.example.aadbackspring.dto.DExApiResponseDTO;
import com.example.aadbackspring.model.DExchange;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import java.util.List;

@Service
public class DExApiService {

    private static final String DEX_API_URL = "https://pro-api.coinmarketcap.com/v4/dex/listings/quotes";
    private static final String CMC_API_KEY = "bab4713c-03bd-4f18-b7f1-ea27196f7cb7";

    private final RestTemplate restTemplate;
    private final DExchangeService dexExchangeService; // for fallback

    @Autowired
    public DExApiService(RestTemplateBuilder builder, DExchangeService dexExchangeService) {
        this.restTemplate = builder.build();
        this.dexExchangeService = dexExchangeService;
    }

    @CircuitBreaker(name = "coinmarketcap", fallbackMethod = "dexApiFallback")
    public List<DExchange> getDexListings() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", CMC_API_KEY);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<DExApiResponseDTO> response = restTemplate.exchange(
                DEX_API_URL,
                HttpMethod.GET,
                entity,
                DExApiResponseDTO.class
        );
        // Assuming ApiResponse is a DTO representing the response body from the API.
        return response.getBody().getData();
    }

    // Fallback method triggered if the external API call fails.
    public List<DExchange> dexApiFallback(Throwable t) {
        // Optionally log the exception 't'
        System.err.println("External API call failed: " + t.getMessage());
        // Return local data from your already mocked API/data store.
        return dexExchangeService.getAllDExchanges();
    }
}
