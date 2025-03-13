package com.example.aadbackspring.repository.stripe;

import com.example.aadbackspring.model.stripe.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    long count();

    List<SubscriptionPlan> findAll();

    Optional<SubscriptionPlan> findByPriceId(String priceId);
}
