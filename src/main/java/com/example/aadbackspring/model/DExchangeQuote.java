package com.example.aadbackspring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Embeddable
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DExchangeQuote {

    @JsonProperty("convert_id")
    private String convertId;

    @JsonProperty("market_type")
    private String marketType;

    @JsonProperty("last_updated")
    private OffsetDateTime lastUpdated;

    @JsonProperty("volume_24h")
    private Double volume24h;

    @JsonProperty("percent_change_volume_24h")
    private Double percentChangeVolume24h;

    @JsonProperty("num_transactions_24h")
    private Long numTransactions24h;
}