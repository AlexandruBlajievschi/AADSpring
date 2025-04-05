package com.example.aadbackspring.controller;

import com.example.aadbackspring.model.AppConfiguration;
import com.example.aadbackspring.service.AppConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class ConfigurationController {

    @Autowired
    private AppConfigurationService configService;

    @GetMapping
    public ResponseEntity<AppConfiguration> getConfig() {
        AppConfiguration config = configService.getConfig();
        return ResponseEntity.ok(config);
    }

    @PutMapping
    public ResponseEntity<AppConfiguration> updateConfig(@RequestBody AppConfiguration updatedConfig) {
        AppConfiguration config = configService.updateConfig(
                updatedConfig.isUseLocalNews(),
                updatedConfig.isUseLocalCoins()
        );
        return ResponseEntity.ok(config);
    }
}
