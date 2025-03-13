package com.example.aadbackspring.service;

import com.example.aadbackspring.exception.UserNotFoundException;
import com.example.aadbackspring.model.User;
import com.example.aadbackspring.model.stripe.StripeSubscriptionResponse;
import com.example.aadbackspring.model.stripe.SubscriptionPlan;
import com.example.aadbackspring.model.stripe.UserSubscription;
import com.example.aadbackspring.model.stripe.SubscriptionRequestDto;
import com.example.aadbackspring.repository.stripe.SubscriptionPlanRepository;
import com.example.aadbackspring.repository.UserRepository;
import com.example.aadbackspring.repository.stripe.UserSubscriptionRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;

@Service
public class StripeService {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    private final UserRepository userRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    public StripeService(@Value("${stripe.secret-key}") String stripeSecretKey,
                         UserRepository userRepository, SubscriptionPlanRepository subscriptionPlanRepository, UserSubscriptionRepository userSubscriptionRepository) {
        this.stripeSecretKey = stripeSecretKey;
        this.userRepository = userRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.userSubscriptionRepository = userSubscriptionRepository;
        // For all server-side calls we use the secret key.
        Stripe.apiKey = stripeSecretKey;
    }

    public StripeSubscriptionResponse createSubscription(SubscriptionRequestDto subscriptionRequest) {
        // 0. Check for existing user in the database.
        Optional<User> userEmail = userRepository.findByEmail(subscriptionRequest.getEmail());
        Optional<User> userUsername = userRepository.findByUsername(subscriptionRequest.getUsername());

        if (userEmail.isEmpty() || userUsername.isEmpty()) {
            throw new UserNotFoundException("Your email or username is incorrect, please verify them and try again.");
        }

        User user = userUsername.get();

        // Look up the subscription plan based on the provided priceId.
        Optional<SubscriptionPlan> optPlan = subscriptionPlanRepository.findByPriceId(subscriptionRequest.getPriceId());
        if (optPlan.isEmpty()) {
            throw new RuntimeException("Subscription plan not found for priceId: " + subscriptionRequest.getPriceId());
        }
        SubscriptionPlan plan = optPlan.get();

        // Check if the user already has an active subscription of that plan.
        Optional<UserSubscription> existingSubscription =
                userSubscriptionRepository.findByUserAndSubscriptionPlanAndStatus(user, plan, "active");
        if (existingSubscription.isPresent()) {
            throw new RuntimeException("You already have an active subscription for this plan.");
        }

        try {
            // 1. Create a Customer using the email and username provided by the client.
            Map<String, Object> customerParams = new HashMap<>();
            customerParams.put("email", subscriptionRequest.getEmail());
            customerParams.put("name", subscriptionRequest.getUsername());
            customerParams.put("payment_method", subscriptionRequest.getPaymentMethodId());
            Customer customer = Customer.create(customerParams);

            // 2. Attach the PaymentMethod to the Customer.
            PaymentMethod paymentMethod = PaymentMethod.retrieve(subscriptionRequest.getPaymentMethodId());
            Map<String, Object> attachParams = new HashMap<>();
            attachParams.put("customer", customer.getId());
            paymentMethod.attach(attachParams);

            // 3. Update the Customer’s invoice settings to set the default payment method.
            Map<String, Object> invoiceSettings = new HashMap<>();
            invoiceSettings.put("default_payment_method", subscriptionRequest.getPaymentMethodId());
            Map<String, Object> customerUpdateParams = new HashMap<>();
            customerUpdateParams.put("invoice_settings", invoiceSettings);
            customer = customer.update(customerUpdateParams);

            // 4. Create the Subscription with the provided priceId and quantity.
            List<Object> items = new ArrayList<>();
            Map<String, Object> item = new HashMap<>();
            item.put("price", subscriptionRequest.getPriceId());
            item.put("quantity", subscriptionRequest.getNumberOfLicenses());
            items.add(item);
            Map<String, Object> subscriptionParams = new HashMap<>();
            subscriptionParams.put("customer", customer.getId());
            subscriptionParams.put("items", items);
            subscriptionParams.put("default_payment_method", subscriptionRequest.getPaymentMethodId());
            Subscription subscription = Subscription.create(subscriptionParams);

            // 5. Save a join record (UserSubscription) in your database.
            // Look up the subscription plan using the priceId.
//            Optional<SubscriptionPlan> optPlan = subscriptionPlanRepository.findByPriceId(subscriptionRequest.getPriceId());
//            if (optPlan.isEmpty()) {
//                throw new RuntimeException("Subscription plan not found for priceId: " + subscriptionRequest.getPriceId());
//            }
//            SubscriptionPlan plan = optPlan.get();
            UserSubscription userSubscription = new UserSubscription();
            userSubscription.setUser(user);
            userSubscription.setSubscriptionPlan(plan);
            userSubscription.setStripeSubscriptionId(subscription.getId());
            userSubscription.setStripeCustomerId(customer.getId());
            userSubscription.setStatus(subscription.getStatus());
            // Convert epoch seconds from Stripe to Java Instant.
            userSubscription.setStartDate(Instant.ofEpochSecond(subscription.getCurrentPeriodStart()));
            userSubscription.setEndDate(Instant.ofEpochSecond(subscription.getCurrentPeriodEnd()));
            userSubscriptionRepository.save(userSubscription);

            // 6. Build and return the response.
            StripeSubscriptionResponse response = new StripeSubscriptionResponse();
            response.setStripeCustomerId(customer.getId());
            response.setStripeSubscriptionId(subscription.getId());
            response.setStripePaymentMethodId(subscriptionRequest.getPaymentMethodId());
            response.setUsername(subscriptionRequest.getUsername());
            return response;
        } catch (StripeException e) {
            throw new RuntimeException("Stripe error: " + e.getMessage(), e);
        }
    }

    public Subscription cancelSubscription(String subscriptionId) {
        try {
            Subscription subscription = Subscription.retrieve(subscriptionId);
            Subscription cancelled = subscription.cancel();

            // 1. Find the corresponding UserSubscription record by stripe_subscription_id.
            Optional<UserSubscription> optUserSubscription = userSubscriptionRepository.findByStripeSubscriptionId(subscriptionId);
            if (optUserSubscription.isPresent()) {
                UserSubscription userSubscription = optUserSubscription.get();
                // 2. Update its status to "canceled" (or "cancel", based on your convention).
                userSubscription.setStatus(cancelled.getStatus());
                // Optionally update the endDate if Stripe returns a new value.
                userSubscription.setEndDate(Instant.ofEpochSecond(cancelled.getCurrentPeriodEnd()));
                userSubscriptionRepository.save(userSubscription);
            }
            return cancelled;
        } catch (StripeException e) {
            throw new RuntimeException("Failed to cancel subscription: " + e.getMessage(), e);
        }
    }
}