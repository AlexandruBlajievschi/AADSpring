package com.example.aadbackspring.model.stripe;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionRequestDto {
    private String paymentMethodId; // Received from client after tokenization
    private String email;
    private String priceId;
    private String username;
    private int numberOfLicenses; // quantity, e.g., number of licenses

    public SubscriptionRequestDto() {
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNumberOfLicenses() {
        return numberOfLicenses;
    }

    public void setNumberOfLicenses(int numberOfLicenses) {
        this.numberOfLicenses = numberOfLicenses;
    }
}