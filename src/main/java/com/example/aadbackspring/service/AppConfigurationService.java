package com.example.aadbackspring.service;

import com.example.aadbackspring.model.AppConfiguration;
import com.example.aadbackspring.repository.AppConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppConfigurationService {

    @Autowired
    private AppConfigurationRepository configRepository;

    public AppConfiguration getConfig() {
        return configRepository.findAll().get(0);
    }

    public AppConfiguration updateConfig(boolean useLocalNews, boolean useLocalCoins) {
        AppConfiguration config = getConfig();
        config.setUseLocalNews(useLocalNews);
        config.setUseLocalCoins(useLocalCoins);
        return configRepository.save(config);
    }
}

