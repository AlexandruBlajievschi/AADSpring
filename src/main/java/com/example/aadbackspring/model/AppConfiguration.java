package com.example.aadbackspring.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class AppConfiguration {
    // Getters/Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private boolean useLocalNews;
    @Setter
    private boolean useLocalCoins;

    // Constructors
    public AppConfiguration() {}
    public AppConfiguration(boolean useLocalNews, boolean useLocalCoins) {
        this.useLocalNews = useLocalNews;
        this.useLocalCoins = useLocalCoins;
    }


}
