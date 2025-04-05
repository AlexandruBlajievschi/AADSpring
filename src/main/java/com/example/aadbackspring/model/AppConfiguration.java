package com.example.aadbackspring.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
public class AppConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean useLocalNews;
    private boolean useLocalCoins;

    public AppConfiguration() {}
    public AppConfiguration(boolean useLocalNews, boolean useLocalCoins) {
        this.useLocalNews = useLocalNews;
        this.useLocalCoins = useLocalCoins;
    }


}
