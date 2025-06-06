package com.example.aadbackspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "coins")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @JsonProperty("id")
    private Long externalId;

    private String name;
    private String symbol;
    private Double price;

    @JsonProperty("percent_change_24h")
    private Double percentChange24h;
}