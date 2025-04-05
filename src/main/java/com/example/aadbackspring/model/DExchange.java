package com.example.aadbackspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "dexchanges")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DExchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @JsonProperty("id")
    private Long externalId;

    @JsonProperty("num_market_pairs")
    private String numMarketPairs;

    @JsonProperty("last_updated")
    private OffsetDateTime lastUpdated;

    @JsonProperty("market_share")
    private Double marketShare;

    private String type;

    @ElementCollection
    @CollectionTable(name = "dexchange_quote", joinColumns = @JoinColumn(name = "dexchange_id"))
    private List<DExchangeQuote> quote;

    private String name;
    private String slug;
    private String status;
}