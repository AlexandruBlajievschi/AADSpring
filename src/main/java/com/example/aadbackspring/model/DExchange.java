package com.example.aadbackspring.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "dexchanges")
public class DExchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Internal primary key

    // This field corresponds to the external API "id"
    private Long externalId;

    // The number of market pairs – note: in the external API it's a string, but you might choose to store it as a string or number.
    private String numMarketPairs;

    private OffsetDateTime lastUpdated;
    private Double marketShare;
    private String type;

    // A collection of quotes
    @ElementCollection
    @CollectionTable(name = "dexchange_quote", joinColumns = @JoinColumn(name = "dexchange_id"))
    private List<DExchangeQuote> quote;

    private String name;
    private String slug;
    private String status;

    // Getters and setters

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
