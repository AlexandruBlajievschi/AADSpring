package com.example.aadbackspring.repository;

import com.example.aadbackspring.model.DExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DExchangeRepository extends JpaRepository<DExchange, Long> {
    // Additional query methods if needed.
}
