package com.example.aadbackspring.repository;

import com.example.aadbackspring.model.AppConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppConfigurationRepository extends JpaRepository<AppConfiguration, Long> {
}
