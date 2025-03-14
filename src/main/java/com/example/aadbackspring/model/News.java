package com.example.aadbackspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "news")
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

    public void setId(Long id) {
        this.id = id;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Long getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(Long publishedOn) {
        this.publishedOn = publishedOn;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }
}
