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

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public UserSubscription() {
    }

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    // ... Getters and setters ...

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public SubscriptionPlan getSubscriptionPlan() {
//        return subscriptionPlan;
//    }
//
//    public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
//        this.subscriptionPlan = subscriptionPlan;
//    }
//
//    public String getStripeSubscriptionId() {
//        return stripeSubscriptionId;
//    }
//
//    public void setStripeSubscriptionId(String stripeSubscriptionId) {
//        this.stripeSubscriptionId = stripeSubscriptionId;
//    }
//
//    public String getStripeCustomerId() {
//        return stripeCustomerId;
//    }
//
//    public void setStripeCustomerId(String stripeCustomerId) {
//        this.stripeCustomerId = stripeCustomerId;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public Instant getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(Instant startDate) {
//        this.startDate = startDate;
//    }
//
//    public Instant getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(Instant endDate) {
//        this.endDate = endDate;
//    }
//
//    public Instant getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(Instant createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public Instant getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(Instant updatedAt) {
//        this.updatedAt = updatedAt;
//    }
}