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
}