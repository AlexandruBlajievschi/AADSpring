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

    // Nested Status DTO
    public static class Status {
        private String timestamp;
        private String error_code;
        private String error_message;
        private int elapsed;
        private int credit_count;

        // Getters and Setters
        public String getTimestamp() {
            return timestamp;
        }
        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
        public String getError_code() {
            return error_code;
        }
        public void setError_code(String error_code) {
            this.error_code = error_code;
        }
        public String getError_message() {
            return error_message;
        }
        public void setError_message(String error_message) {
            this.error_message = error_message;
        }
        public int getElapsed() {
            return elapsed;
        }
        public void setElapsed(int elapsed) {
            this.elapsed = elapsed;
        }
        public int getCredit_count() {
            return credit_count;
        }
        public void setCredit_count(int credit_count) {
            this.credit_count = credit_count;
        }
    }
}