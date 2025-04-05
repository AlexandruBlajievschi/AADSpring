package com.example.aadbackspring.dto;


import com.example.aadbackspring.model.DExchange;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DExApiResponseDTO {

    private List<DExchange> data;
    private Status status;

    @Setter
    @Getter
    public static class Status {
        private String timestamp;
        private String error_code;
        private String error_message;
        private int elapsed;
        private int credit_count;

    }
}