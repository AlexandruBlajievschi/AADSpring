package com.example.aadbackspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "coins")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coin {

    // Internal DB primary key – not exposed to the API response
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    // Maps the external API "id" to our externalId field.
    @JsonProperty("id")
    private Long externalId;

    private String name;
    private String symbol;
    private Double price;

    @JsonProperty("percent_change_24h")
    private Double percentChange24h;

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

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPercentChange24h() {
        return percentChange24h;
    }
    public void setPercentChange24h(Double percentChange24h) {
        this.percentChange24h = percentChange24h;
    }
}