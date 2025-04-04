package com.example.aadbackspring.controller;

import com.example.aadbackspring.model.AppConfiguration;
import com.example.aadbackspring.service.AppConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// If you want to restrict to admin only, use @PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/config")
public class ConfigurationController {

    @Autowired
    private AppConfigurationService configService;

    // GET current config
    @GetMapping
    public ResponseEntity<AppConfiguration> getConfig() {
        AppConfiguration config = configService.getConfig();
        return ResponseEntity.ok(config);
    }

    // PUT update config (admin toggles local or external)
    @PutMapping
    public ResponseEntity<AppConfiguration> updateConfig(@RequestBody AppConfiguration updatedConfig) {
        // updatedConfig will come from React Native admin as JSON
        // e.g., { "useLocalNews": true, "useLocalCoins": false }
        AppConfiguration config = configService.updateConfig(
                updatedConfig.isUseLocalNews(),
                updatedConfig.isUseLocalCoins()
        );
        return ResponseEntity.ok(config);
    }
}
