package com.example.aadbackspring.dto;

import com.example.aadbackspring.model.News;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class NewsApiResponseDTO {

    @JsonProperty("Type")
    private int type;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Promoted")
    private List<Object> promoted;

    @JsonProperty("Data")
    private List<News> data;

    // Getters and setters
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Object> getPromoted() {
        return promoted;
    }

    public void setPromoted(List<Object> promoted) {
        this.promoted = promoted;
    }

    public List<News> getData() {
        return data;
    }

    public void setData(List<News> data) {
        this.data = data;
    }
}
