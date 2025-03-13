package com.example.aadbackspring.config;

import com.example.aadbackspring.model.stripe.SubscriptionPlan;
import com.example.aadbackspring.repository.stripe.SubscriptionPlanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DatabaseInitializer {

    @Bean
    CommandLineRunner initDatabase(SubscriptionPlanRepository repository) {
        return args -> {
            if (repository.count() == 0) { // Only insert if the table is empty
                List<SubscriptionPlan> plans = List.of(
                        new SubscriptionPlan("Basic", "price_1R1U1o4gHyUjWEHaEqEYcyHd", 5.00, "EUR", "Basic subscription")
                );
                repository.saveAll(plans);
                System.out.println("Subscription plans initialized.");
            } else {
                System.out.println("Subscription plans already exist, skipping initialization.");
            }
        };
    }
}