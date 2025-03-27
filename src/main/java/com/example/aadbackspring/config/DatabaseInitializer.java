package com.example.aadbackspring.config;

import com.example.aadbackspring.model.User;
import com.example.aadbackspring.model.stripe.SubscriptionPlan;
import com.example.aadbackspring.repository.UserRepository;
import com.example.aadbackspring.repository.stripe.SubscriptionPlanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Configuration
public class DatabaseInitializer {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    CommandLineRunner initDatabase(
            SubscriptionPlanRepository planRepository,
            UserRepository userRepository
    ) {
        return args -> {
            initializeSubscriptionPlans(planRepository);
            initializeAdminUser(userRepository);
        };
    }

    private void initializeSubscriptionPlans(SubscriptionPlanRepository planRepository) {
        if (planRepository.count() == 0) {
            List<SubscriptionPlan> plans = List.of(
                    new SubscriptionPlan("Basic", "price_1R1U1o4gHyUjWEHaEqEYcyHd", 5.00, "EUR", "Basic subscription")
            );
            planRepository.saveAll(plans);
        }
    }

    private void initializeAdminUser(UserRepository userRepository) {
        System.out.println(adminPassword);
        String encryptedPassword = new PasswordEncoderUtil().encode(adminPassword);

        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail(adminEmail);
            admin.setPassword(encryptedPassword); // Replace in production
            admin.setRole("admin");
            userRepository.save(admin);
        }
    }
}