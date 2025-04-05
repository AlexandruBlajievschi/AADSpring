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

    @Column(length = 1024)
    private String tags;

    private String lang;

    @Column(length = 1024)
    private String categories;

    @Column(length = 1024)
    private String source;


    @JsonIgnore
    public Long getId() {
        return id;
    }
}