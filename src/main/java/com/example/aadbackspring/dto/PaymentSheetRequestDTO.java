package com.example.aadbackspring.dto;

public class PaymentSheetRequestDTO {
    // Minimal data for subscription flow:
    // The user's email and the subscription priceId (from your Stripe dashboard)
    private String email;
    private String priceId;   // e.g. "price_1R1U1o4gHyUjWEHaEqEYcyHd"
    private Integer quantity; // optional; defaults to 1 if not provided

    public PaymentSheetRequestDTO() {}

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
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
