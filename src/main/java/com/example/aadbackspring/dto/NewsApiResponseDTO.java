package com.example.aadbackspring.dto;

import com.example.aadbackspring.model.News;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NewsApiResponseDTO {

    @JsonProperty("Type")
    private int type;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Promoted")
    private List<Object> promoted;

    @JsonProperty("Data")
    private List<News> data;
}