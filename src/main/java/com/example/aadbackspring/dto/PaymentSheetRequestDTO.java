package com.example.aadbackspring.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentSheetRequestDTO {
    // Minimal data for subscription flow:
    // The user's email and the subscription priceId (from your Stripe dashboard)
    private String email;
    private String priceId;   // e.g. "price_1R1U1o4gHyUjWEHaEqEYcyHd"
    private Integer quantity; // optional; defaults to 1 if not provided

    public PaymentSheetRequestDTO() {}
}
