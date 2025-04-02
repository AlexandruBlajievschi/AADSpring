package com.example.aadbackspring.service;

import com.example.aadbackspring.dto.PaymentSheetRequestDTO;
import com.example.aadbackspring.exception.ResourceAlreadyExistsException;
import com.example.aadbackspring.model.User;
import com.example.aadbackspring.model.stripe.SubscriptionPlan;
import com.example.aadbackspring.model.stripe.UserSubscription;
import com.example.aadbackspring.repository.stripe.SubscriptionPlanRepository;
import com.example.aadbackspring.repository.UserRepository;
import com.example.aadbackspring.repository.stripe.UserSubscriptionRepository;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.RequestOptions;
import com.stripe.net.Webhook;
import com.stripe.param.EphemeralKeyCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.*;

@Service
public class StripeService {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    private final UserRepository userRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private static final String STRIPE_API_VERSION = "2025-02-24.acacia";

    public StripeService(@Value("${stripe.secret-key}") String stripeSecretKey,
                         UserRepository userRepository,
                         SubscriptionPlanRepository subscriptionPlanRepository,
                         UserSubscriptionRepository userSubscriptionRepository) {
        this.stripeSecretKey = stripeSecretKey;
        this.userRepository = userRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.userSubscriptionRepository = userSubscriptionRepository;
        Stripe.apiKey = stripeSecretKey;
    }

    public Map<String, String> createPaymentSheet(PaymentSheetRequestDTO request) throws StripeException {
        User user = getUserByEmail(request.getEmail());
        String stripeCustomerId = ensureStripeCustomer(user);

        if (hasActiveSubscription(stripeCustomerId)) {
            throw new ResourceAlreadyExistsException("You already have an active subscription.");
        }

        Subscription subscription = createStripeSubscription(stripeCustomerId, request);
        PaymentIntent paymentIntent = extractPaymentIntent(subscription);
        EphemeralKey ephemeralKey = createEphemeralKey(stripeCustomerId);
        persistUserSubscription(user, request.getPriceId(), subscription);
        return buildPaymentSheetResponse(paymentIntent, ephemeralKey, stripeCustomerId);
    }

    public ResponseEntity<String> processWebhookEvent(String payload, String sigHeader) {
        Event event = constructWebhookEvent(payload, sigHeader);
        dispatchWebhookEvent(event);
        return ResponseEntity.ok("Success");
    }

    public Subscription cancelSubscription(String subscriptionId) {
        try {
            Subscription subscription = Subscription.retrieve(subscriptionId);
            Subscription cancelled = subscription.cancel();

            userSubscriptionRepository.findByStripeSubscriptionId(subscriptionId).ifPresent(userSub -> {
                userSub.setStatus(cancelled.getStatus());
                userSub.setEndDate(Instant.ofEpochSecond(cancelled.getCurrentPeriodEnd()));
                userSubscriptionRepository.save(userSub);
            });
            return cancelled;
        } catch (StripeException e) {
            throw new RuntimeException("Failed to cancel subscription: " + e.getMessage(), e);
        }
    }

    // -------------------------
    // Helper Methods for Payment Sheet Creation
    // -------------------------
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    private String ensureStripeCustomer(User user) throws StripeException {
        String stripeCustomerId = user.getStripeCustomerId();
        if (stripeCustomerId == null || stripeCustomerId.isEmpty()) {
            Map<String, Object> params = new HashMap<>();
            params.put("email", user.getEmail());
            params.put("name", user.getUsername());
            Customer customer = Customer.create(params);
            stripeCustomerId = customer.getId();
            user.setStripeCustomerId(stripeCustomerId);
            userRepository.save(user);
        }
        return stripeCustomerId;
    }

    private boolean hasActiveSubscription(String stripeCustomerId) {
        return userSubscriptionRepository.findByStripeCustomerIdAndStatus(stripeCustomerId, "active").isPresent();
    }

    private Subscription createStripeSubscription(String stripeCustomerId, PaymentSheetRequestDTO request) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("customer", stripeCustomerId);
        List<Object> items = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("price", request.getPriceId());
        if (request.getQuantity() != null) {
            item.put("quantity", request.getQuantity());
        }
        items.add(item);
        params.put("items", items);
        params.put("payment_behavior", "default_incomplete");
        params.put("expand", List.of("latest_invoice.payment_intent"));
        return Subscription.create(params);
    }

    private PaymentIntent extractPaymentIntent(Subscription subscription) throws StripeException {
        String piObj = subscription.getLatestInvoiceObject().getPaymentIntent();
        if (piObj != null) {
            return PaymentIntent.retrieve(piObj);
        }
        throw new RuntimeException("Unexpected type for payment intent: " + piObj.getClass().getName());
    }

    private EphemeralKey createEphemeralKey(String stripeCustomerId) throws StripeException {
        EphemeralKeyCreateParams params = EphemeralKeyCreateParams.builder()
                .setCustomer(stripeCustomerId)
                .setStripeVersion(STRIPE_API_VERSION)
                .build();
        return EphemeralKey.create(params, RequestOptions.getDefault());
    }

    private void persistUserSubscription(User user, String priceId, Subscription subscription) {
        SubscriptionPlan plan = subscriptionPlanRepository.findByPriceId(priceId)
                .orElseThrow(() -> new RuntimeException("Subscription plan not found for priceId: " + priceId));
        UserSubscription userSub = new UserSubscription();
        userSub.setUser(user);
        userSub.setSubscriptionPlan(plan);
        userSub.setStripeSubscriptionId(subscription.getId());
        userSub.setStripeCustomerId(user.getStripeCustomerId());
        userSub.setStatus(subscription.getStatus());
        userSub.setStartDate(Instant.ofEpochSecond(subscription.getCurrentPeriodStart()));
        userSub.setEndDate(Instant.ofEpochSecond(subscription.getCurrentPeriodEnd()));
        userSubscriptionRepository.save(userSub);
    }

    private Map<String, String> buildPaymentSheetResponse(PaymentIntent paymentIntent, EphemeralKey ephemeralKey, String customerId) {
        Map<String, String> responseData = new HashMap<>();
        responseData.put("paymentIntent", paymentIntent.getClientSecret());
        responseData.put("ephemeralKey", ephemeralKey.getSecret());
        responseData.put("customer", customerId);
        return responseData;
    }

    private Map<String, String> errorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }

    // -------------------------
    // Helper Methods for Webhook Processing
    // -------------------------
    private Event constructWebhookEvent(String payload, String sigHeader) {
        try {
            return Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            throw new RuntimeException("Webhook signature verification failed: " + e.getMessage());
        }
    }

    private void dispatchWebhookEvent(Event event) {
        switch (event.getType()) {
            case "invoice.payment_succeeded":
                handleInvoicePaymentSucceeded((Invoice) event.getDataObjectDeserializer().getObject().orElse(null));
                break;
            case "customer.subscription.updated":
                handleSubscriptionUpdated((Subscription) event.getDataObjectDeserializer().getObject().orElse(null));
                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());
                break;
        }
    }

    private void handleInvoicePaymentSucceeded(Invoice invoice) {
        if (invoice != null && invoice.getSubscription() != null) {
            String subscriptionId = invoice.getSubscription();
            userSubscriptionRepository.findByStripeSubscriptionId(subscriptionId).ifPresent(userSub -> {
                userSub.setStatus("active");
                userSub.setStartDate(Instant.ofEpochSecond(invoice.getPeriodStart()));
                userSub.setEndDate(Instant.ofEpochSecond(invoice.getPeriodEnd()));
                userSubscriptionRepository.save(userSub);
            });
        }
    }

    private void handleSubscriptionUpdated(Subscription subscription) {
        if (subscription != null) {
            String subscriptionId = subscription.getId();
            userSubscriptionRepository.findByStripeSubscriptionId(subscriptionId).ifPresent(userSub -> {
                userSub.setStatus(subscription.getStatus());
                userSub.setStartDate(Instant.ofEpochSecond(subscription.getCurrentPeriodStart()));
                userSub.setEndDate(Instant.ofEpochSecond(subscription.getCurrentPeriodEnd()));
                userSubscriptionRepository.save(userSub);
            });
        }
    }
}