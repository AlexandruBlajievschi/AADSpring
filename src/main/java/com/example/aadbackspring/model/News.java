package com.example.aadbackspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "news")
@Getter
@Setter
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Increase length or use TEXT for externalId if needed
    @JsonProperty("id")
    @Column(length = 512)
    private String externalId;

    @Column(length = 1024)  // GUIDs can be long URLs with parameters.
    private String guid;

    @JsonProperty("published_on")
    private Long publishedOn;

    private String imageurl;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String url;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(length = 1024) // In case tags are concatenated or very descriptive.
    private String tags;

    private String lang;

    @Column(length = 1024) // To allow more categories if needed.
    private String categories;

    @Column(length = 1024) // In case the source name or details become longer.
    private String source;

    // Getters and setters

    @JsonIgnore  // Ignore the internal database id when serializing/deserializing
    public Long getId() {
        return id;
    }
}