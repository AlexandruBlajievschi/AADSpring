package com.example.aadbackspring.repository.stripe;

import com.example.aadbackspring.model.stripe.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    Optional<UserSubscription> findByStripeSubscriptionId(String stripeSubscriptionId);
    Optional<UserSubscription> findByStripeCustomerIdAndStatus(String stripeCustomerId, String status);
    Optional<List<UserSubscription>> findByUserId(Long userId);
}