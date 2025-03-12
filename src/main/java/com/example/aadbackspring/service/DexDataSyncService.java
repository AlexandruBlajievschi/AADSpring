package com.example.aadbackspring.service;

import com.example.aadbackspring.model.DExchange;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DexDataSyncService {

    private final DexApiService dexApiService;
    private final DExchangeService dexExchangeService;

    public DexDataSyncService(DexApiService dexApiService, DExchangeService dexExchangeService) {
        this.dexApiService = dexApiService;
        this.dexExchangeService = dexExchangeService;
    }

    // This scheduled task runs every 24 hours (adjust the cron as needed)
    @Scheduled(cron = "0 0 0 * * ?") // Every day at midnight
    @Transactional
    public void syncDexData() {
        try {
            // Fetch data from the external API
            List<DExchange> externalDexes = dexApiService.getDexListings();
            // Upsert logic: update existing records, insert new ones, and optionally remove old ones
            dexExchangeService.upsertDexExchanges(externalDexes);
            System.out.println("Dex data sync completed successfully at " + System.currentTimeMillis());
        } catch (Exception e) {
            System.err.println("Dex data sync failed: " + e.getMessage());
            // You might log the error or alert your ops team
        }
    }
}
