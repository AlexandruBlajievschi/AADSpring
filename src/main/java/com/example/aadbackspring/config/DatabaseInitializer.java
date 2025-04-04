package com.example.aadbackspring.config;

import com.example.aadbackspring.model.AppConfiguration;
import com.example.aadbackspring.model.User;
import com.example.aadbackspring.model.stripe.SubscriptionPlan;
import com.example.aadbackspring.repository.AppConfigurationRepository;
import com.example.aadbackspring.repository.UserRepository;
import com.example.aadbackspring.repository.stripe.SubscriptionPlanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;

// Import our password encoder utility.
import com.example.aadbackspring.config.PasswordEncoderUtil;

@Configuration
@ConditionalOnProperty(name = "database.initializer.enabled", havingValue = "true", matchIfMissing = true)
public class DatabaseInitializer {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${default.user.email}")
    private String userEmail;

    @Value("${default.user.password}")
    private String userPassword;

    @Bean
    CommandLineRunner initDatabase(SubscriptionPlanRepository planRepository, UserRepository userRepository, AppConfigurationRepository configRepository ) {
        return args -> {
            initializeSubscriptionPlans(planRepository);
            initializeAdminUser(userRepository);
            initializeDefaultUser(userRepository);
            initializeAppConfiguration(configRepository);
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
        String encryptedPassword = new PasswordEncoderUtil().encode(adminPassword);
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail(adminEmail);
            admin.setPassword(encryptedPassword);
            admin.setRole("admin");
            userRepository.save(admin);
        }
    }

    private void initializeDefaultUser(UserRepository userRepository) {
        String encryptedPassword = new PasswordEncoderUtil().encode(userPassword);
        if (userRepository.findByEmail(userEmail).isEmpty()) {
            User user = new User();
            user.setUsername("user");
            user.setEmail(userEmail);
            user.setPassword(encryptedPassword);
            user.setRole("user");
            userRepository.save(user);
        }

    }
    private void initializeAppConfiguration(AppConfigurationRepository configRepository) {
        if (configRepository.count() == 0) {
            // Create a default configuration record
            AppConfiguration defaultConfig = new AppConfiguration(false, false);
            configRepository.save(defaultConfig);
        }
    }
}
