package com.example.aadbackspring.service;

import com.example.aadbackspring.dto.CoinApiResponseDTO;
import com.example.aadbackspring.exception.ResourceNotFoundException;
import com.example.aadbackspring.model.Coin;
import com.example.aadbackspring.repository.CoinRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CoinService {

    private static final Logger logger = LoggerFactory.getLogger(CoinService.class);

    private final CoinRepository repository;
    private final RestTemplate restTemplate;

    @Value("${external.coins.api.url}")
    private String externalCoinApiUrl;

    @Value("${cmc.api.key}")
    private String cmcApiKey;

    public CoinService(CoinRepository repository, RestTemplateBuilder restTemplateBuilder) {
        this.repository = repository;
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * External API call to fetch coin data.
     */
    @CircuitBreaker(name = "coinmarketcap", fallbackMethod = "coinApiFallback")
    public List<Coin> getCoinsFromExternal() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", cmcApiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<CoinApiResponseDTO> response = restTemplate.exchange(
                externalCoinApiUrl,
                HttpMethod.GET,
                entity,
                CoinApiResponseDTO.class
        );

        if (response.getStatusCode().is2xxSuccessful()
                && response.getBody() != null
                && response.getBody().getData() != null) {

            List<Coin> coins = new ArrayList<>();
            for (CoinApiResponseDTO.CoinData coinData : response.getBody().getData()) {
                Coin coin = new Coin();
                coin.setExternalId(coinData.getId());
                coin.setName(coinData.getName());
                coin.setSymbol(coinData.getSymbol());
                coin.setPrice(coinData.getQuote().getUSD().getPrice());
                coin.setPercentChange24h(coinData.getQuote().getUSD().getPercentChange24h());
                coins.add(coin);
            }
            return coins;
        } else {
            throw new ResourceNotFoundException("External Coin API resource not found");
        }
    }

    public List<Coin> coinApiFallback(Throwable t) {
        logger.error("External Coin API call failed: {}", t.getMessage());
        return repository.findAll();
    }

    /**
     * Unified method that attempts to fetch coin data from the external API.
     */
    public List<Coin> getCoinListings() {
        try {
            return getCoinsFromExternal();
        } catch (Exception ex) {
            logger.warn("Falling back to local coin data: {}", ex.getMessage());
            return repository.findAll();
        }
    }

    // ---- Manual CRUD methods ----

    public Coin createCoin(Coin coin) {
        return repository.save(coin);
    }

    public Optional<Coin> getCoinById(Long id) {
        return repository.findById(id);
    }

    public Coin updateCoin(Long id, Coin coinDetails) {
        Coin existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coin not found with id " + id));
        existing.setExternalId(coinDetails.getExternalId());
        existing.setName(coinDetails.getName());
        existing.setSymbol(coinDetails.getSymbol());
        existing.setPrice(coinDetails.getPrice());
        existing.setPercentChange24h(coinDetails.getPercentChange24h());
        return repository.save(existing);
    }

    public boolean deleteCoin(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    // ---- Scheduled Data Synchronization ----

    /**
     * Scheduled task to sync external coin data into the local database.
     * (For testing you might run it every minute; later change it to run daily.)
     */
    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void syncCoinData() {
        try {
            logger.info("Running scheduled task to sync coin data.");
            List<Coin> externalCoins = getCoinsFromExternal();
            upsertCoins(externalCoins);
            logger.info("Coin data sync completed successfully with {} records.", externalCoins.size());
        } catch (Exception e) {
            logger.error("Coin data sync failed: {}", e.getMessage());
        }
    }

    /**
     * Upsert logic: update existing coins (found by externalId) or insert new records.
     */
    @Transactional
    public void upsertCoins(List<Coin> externalCoins) {
        for (Coin externalCoin : externalCoins) {
            Optional<Coin> existingOpt = repository.findByExternalId(externalCoin.getExternalId());
            if (existingOpt.isPresent()) {
                Coin existing = existingOpt.get();
                existing.setName(externalCoin.getName());
                existing.setSymbol(externalCoin.getSymbol());
                existing.setPrice(externalCoin.getPrice());
                existing.setPercentChange24h(externalCoin.getPercentChange24h());
                repository.save(existing);
            } else {
                repository.save(externalCoin);
            }
        }
    }

    public List<Coin> getLocalCoins() {
        return repository.findAll();
    }

}
