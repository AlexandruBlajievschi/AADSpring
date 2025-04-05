package com.example.aadbackspring.model.stripe;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subscription_plans")
@Getter
@Setter
@AllArgsConstructor
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(unique = true, nullable = false)
    @JsonProperty("name")
    private String name;

    @Column(unique = true, nullable = false)
    @JsonProperty("price_id")
    private String priceId; // Stripe Price ID

    @Column(nullable = false)
    @JsonProperty("price")
    private double price;

    @Column(nullable = false)
    @JsonProperty("currency")
    private String currency = "EUR";

    @Column
    @JsonProperty("description")
    private String description;

    public SubscriptionPlan(String name, String priceId, double price, String currency, String description) {
        this.name = name;
        this.priceId = priceId;
        this.price = price;
        this.currency = currency;
        this.description = description;
    }

    public SubscriptionPlan() {
    }
}