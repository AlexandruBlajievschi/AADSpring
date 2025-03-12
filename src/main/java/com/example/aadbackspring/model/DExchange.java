package com.example.aadbackspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "dexchanges")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DExchange {

    // DB primary key – not exposed in the JSON response from the external API
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    // This field maps the external API "id" to our externalId field.
    @JsonProperty("id")
    private Long externalId;

    @JsonProperty("num_market_pairs")
    private String numMarketPairs;

    @JsonProperty("last_updated")
    private OffsetDateTime lastUpdated;

    @JsonProperty("market_share")
    private Double marketShare;

    // The "type" field is assumed to be identical in JSON and in our model.
    private String type;

    // Map the external API's "quote" array to our list.
    @ElementCollection
    @CollectionTable(name = "dexchange_quote", joinColumns = @JoinColumn(name = "dexchange_id"))
    private List<DExchangeQuote> quote;

    private String name;
    private String slug;
    private String status;

    // Getters and Setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getExternalId() {
        return externalId;
    }
    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public String getNumMarketPairs() {
        return numMarketPairs;
    }
    public void setNumMarketPairs(String numMarketPairs) {
        this.numMarketPairs = numMarketPairs;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Double getMarketShare() {
        return marketShare;
    }
    public void setMarketShare(Double marketShare) {
        this.marketShare = marketShare;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public List<DExchangeQuote> getQuote() {
        return quote;
    }
    public void setQuote(List<DExchangeQuote> quote) {
        this.quote = quote;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }
    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
