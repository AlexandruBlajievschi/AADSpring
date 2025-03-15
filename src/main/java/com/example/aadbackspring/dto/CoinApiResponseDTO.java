package com.example.aadbackspring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CoinApiResponseDTO {

    private List<CoinData> data;
    private Status status;

    // Getters and setters
    public List<CoinData> getData() {
        return data;
    }
    public void setData(List<CoinData> data) {
        this.data = data;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    // Nested DTO to map each coin's data
    public static class CoinData {
        private Long id;
        private String name;
        private String symbol;
        private Quote quote;

        // Getters and setters
        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
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
        public Quote getQuote() {
            return quote;
        }
        public void setQuote(Quote quote) {
            this.quote = quote;
        }

        public static class Quote {
            @JsonProperty("USD")
            private USD USD;

            public USD getUSD() {
                return USD;
            }
            public void setUSD(USD USD) {
                this.USD = USD;
            }

            public static class USD {
                private Double price;
                @JsonProperty("percent_change_24h")
                private Double percentChange24h;

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
        }
    }

    // Optionally, include a nested Status class if you need to map status details
    public static class Status {
        private String timestamp;
        @JsonProperty("error_code")
        private String errorCode;
        @JsonProperty("error_message")
        private String errorMessage;
        private int elapsed;
        @JsonProperty("credit_count")
        private int creditCount;

        // Getters and setters
        public String getTimestamp() {
            return timestamp;
        }
        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
        public String getErrorCode() {
            return errorCode;
        }
        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }
        public String getErrorMessage() {
            return errorMessage;
        }
        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
        public int getElapsed() {
            return elapsed;
        }
        public void setElapsed(int elapsed) {
            this.elapsed = elapsed;
        }
        public int getCreditCount() {
            return creditCount;
        }
        public void setCreditCount(int creditCount) {
            this.creditCount = creditCount;
        }
    }
}
