package com.example.aadbackspring.model.stripe;

import com.example.aadbackspring.model.User;
import com.example.aadbackspring.model.stripe.SubscriptionPlan;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "user_subscriptions")
@Getter
@Setter
public class UserSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many subscriptions can belong to one user
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    // Many subscriptions can reference one subscription plan
    @ManyToOne(optional = false)
    @JoinColumn(name = "subscription_plan_id")
    private SubscriptionPlan subscriptionPlan;

    @Column(name = "stripe_subscription_id", nullable = false)
    private String stripeSubscriptionId;

    @Column(name = "stripe_customer_id", nullable = false)
    private String stripeCustomerId;

    @Column(nullable = false)
    private String status;

    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public UserSubscription() {
    }

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }
}