package com.example.aadbackspring.service;

import com.example.aadbackspring.dto.DExApiResponseDTO;
import com.example.aadbackspring.exception.ResourceNotFoundException;
import com.example.aadbackspring.model.DExchange;
import com.example.aadbackspring.repository.DExchangeRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class DExchangeService {

    private static final Logger logger = LoggerFactory.getLogger(DExchangeService.class);

    private final DExchangeRepository repository;
    private final RestTemplate restTemplate;

    // External API URL for DEX listings is now in application.properties
    @Value("${external.dex.api.url}")
    private String externalDexApiUrl;

    @Value("${cmc.api.key}")
    private String cmcApiKey;

    public DExchangeService(DExchangeRepository repository, RestTemplateBuilder restTemplateBuilder) {
        this.repository = repository;
        this.restTemplate = restTemplateBuilder.build();
    }

    // ---------------------------
    // CRUD Methods
    // ---------------------------
    public DExchange createDExchange(DExchange dexchange) {
        return repository.save(dexchange);
    }

    public List<DExchange> getAllDExchanges() {
        return repository.findAll();
    }

    public Optional<DExchange> getDExchangeById(Long id) {
        return repository.findById(id);
    }

    public Optional<DExchange> updateDExchange(Long id, DExchange exchangeDetails) {
        return repository.findById(id).map(existing -> {
            existing.setExternalId(exchangeDetails.getExternalId());
            existing.setNumMarketPairs(exchangeDetails.getNumMarketPairs());
            existing.setLastUpdated(exchangeDetails.getLastUpdated());
            existing.setMarketShare(exchangeDetails.getMarketShare());
            existing.setType(exchangeDetails.getType());
            existing.setQuote(exchangeDetails.getQuote());
            existing.setName(exchangeDetails.getName());
            existing.setSlug(exchangeDetails.getSlug());
            existing.setStatus(exchangeDetails.getStatus());
            return repository.save(existing);
        });
    }

    public boolean deleteDExchange(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    // ---------------------------
    // External API Integration
    // ---------------------------

    /**
     * Calls the external DEX API using the URL and API key, and returns a list of DExchange objects.
     * If the API response is not successful or missing data, a ResourceNotFoundException is thrown.
     */
    @CircuitBreaker(name = "coinmarketcap", fallbackMethod = "dexApiFallback")
    public List<DExchange> getDexListingsFromExternal() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", cmcApiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<DExApiResponseDTO> response = restTemplate.exchange(
                externalDexApiUrl,
                HttpMethod.GET,
                entity,
                DExApiResponseDTO.class
        );

        if (response.getStatusCode().is2xxSuccessful()
                && response.getBody() != null
                && response.getBody().getData() != null) {
            return response.getBody().getData();
        } else {
            throw new ResourceNotFoundException("External DExchange API resource not found");
        }
    }

    /**
     * Fallback method for getDexListingsFromExternal.
     * Returns local data from the repository.
     */
    public List<DExchange> dexApiFallback(Throwable t) {
        logger.error("External API call failed: {}", t.getMessage());
        return getAllDExchanges();
    }

    /**
     * Unified method that attempts to fetch data from the external API.
     * If that fails, falls back to local repository data.
     */
    public List<DExchange> getDexListings() {
        try {
            return getDexListingsFromExternal();
        } catch (Exception ex) {  // catch all exceptions, not only ResourceNotFoundException
            logger.warn("Falling back to local DExchange data: {}", ex.getMessage());
            return getAllDExchanges();
        }
    }

    // ---------------------------
    // Data Synchronization (Scheduled)
    // ---------------------------

    /**
     * Scheduled task that runs daily at midnight to sync external DEX data into the local database.
     */
    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void syncDexData() {
        try {
            logger.info("Running scheduled task to sync DExchange data.");
            List<DExchange> externalDexes = getDexListingsFromExternal();
            upsertDexExchanges(externalDexes);
            logger.info("Dex data sync completed successfully with {} records.", externalDexes.size());
        } catch (Exception e) {
            logger.error("Dex data sync failed: {}", e.getMessage());
        }
    }

    /**
     * Upsert logic: For each external DExchange record, update the existing one (found by externalId) or insert a new record.
     */
    @Transactional
    public void upsertDexExchanges(List<DExchange> externalDexes) {
        for (DExchange externalDex : externalDexes) {
            Optional<DExchange> existingOpt = repository.findByExternalId(externalDex.getExternalId());
            if (existingOpt.isPresent()) {
                DExchange existing = existingOpt.get();
                existing.setNumMarketPairs(externalDex.getNumMarketPairs());
                existing.setLastUpdated(externalDex.getLastUpdated());
                existing.setMarketShare(externalDex.getMarketShare());
                existing.setType(externalDex.getType());
                existing.setQuote(externalDex.getQuote());
                existing.setName(externalDex.getName());
                existing.setSlug(externalDex.getSlug());
                existing.setStatus(externalDex.getStatus());
                repository.save(existing);
            } else {
                repository.save(externalDex);
            }
        }
        // Optionally, you can remove records that no longer exist in the external data.
    }
}
