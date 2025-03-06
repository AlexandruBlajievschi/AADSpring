package com.example.aadbackspring.model;

import jakarta.persistence.Embeddable;
import java.time.OffsetDateTime;

@Embeddable
public class DExchangeQuote {

    private String convertId;
    private String marketType;
    private OffsetDateTime lastUpdated;
    private Double volume24h;
    private Double percentChangeVolume24h;
    private Long numTransactions24h;

    // Getters and setters

    public String getConvertId() {
        return convertId;
    }

    public void setConvertId(String convertId) {
        this.convertId = convertId;
    }

    public String getMarketType() {
        return marketType;
    }

    public void setMarketType(String marketType) {
        this.marketType = marketType;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Double getVolume24h() {
        return volume24h;
    }

    public void setVolume24h(Double volume24h) {
        this.volume24h = volume24h;
    }

    public Double getPercentChangeVolume24h() {
        return percentChangeVolume24h;
    }

    public void setPercentChangeVolume24h(Double percentChangeVolume24h) {
        this.percentChangeVolume24h = percentChangeVolume24h;
    }

    public Long getNumTransactions24h() {
        return numTransactions24h;
    }

    public void setNumTransactions24h(Long numTransactions24h) {
        this.numTransactions24h = numTransactions24h;
    }
}
