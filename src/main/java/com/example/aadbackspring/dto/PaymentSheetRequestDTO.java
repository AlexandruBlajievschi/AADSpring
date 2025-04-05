package com.example.aadbackspring.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentSheetRequestDTO {

    private String email;
    private String priceId;
    private Integer quantity;

    public PaymentSheetRequestDTO() {}
}
