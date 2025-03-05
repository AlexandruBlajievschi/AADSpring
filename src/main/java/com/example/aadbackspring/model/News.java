package com.example.aadbackspring.model;

import jakarta.persistence.*;

@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Internal primary key

    @Column(nullable = false, length = 10)
    private String type;

    @Column(name = "source_key", nullable = false, unique = true, length = 50)
    private String sourceKey;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(length = 255)
    private String url;

    @Column(length = 10)
    private String lang;

    @Column(name = "source_type", length = 50)
    private String sourceType;

    @Column(name = "launch_date")
    private Long launchDate;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "benchmark_score")
    private Integer benchmarkScore;

    @Column(length = 20)
    private String status;

    @Column(name = "last_updated_ts")
    private Long lastUpdatedTs;

    @Column(name = "created_on")
    private Long createdOn;

    @Column(name = "updated_on")
    private Long updatedOn;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Long launchDate) {
        this.launchDate = launchDate;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getBenchmarkScore() {
        return benchmarkScore;
    }

    public void setBenchmarkScore(Integer benchmarkScore) {
        this.benchmarkScore = benchmarkScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getLastUpdatedTs() {
        return lastUpdatedTs;
    }

    public void setLastUpdatedTs(Long lastUpdatedTs) {
        this.lastUpdatedTs = lastUpdatedTs;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Long updatedOn) {
        this.updatedOn = updatedOn;
    }
}
