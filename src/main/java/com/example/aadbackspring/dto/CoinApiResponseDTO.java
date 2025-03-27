package com.example.aadbackspring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CoinApiResponseDTO {

    private List<CoinData> data;
    private Status status;

    // Nested DTO to map each coin's data
    @Getter
    @Setter
    public static class CoinData {
        private Long id;
        private String name;
        private String symbol;
        private Quote quote;

        @Getter
        @Setter
        public static class Quote {
            @JsonProperty("USD")
            private USD USD;

            @Getter
            @Setter
            public static class USD {
                private Double price;
                @JsonProperty("percent_change_24h")
                private Double percentChange24h;
            }
        }
    }

    // Optionally, include a nested Status class if you need to map status details
    @Setter
    @Getter
    public static class Status {
        // Getters and setters
        private String timestamp;
        @JsonProperty("error_code")
        private String errorCode;
        @JsonProperty("error_message")
        private String errorMessage;
        private int elapsed;
        @JsonProperty("credit_count")
        private int creditCount;

    }
}