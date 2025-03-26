package com.example.aadbackspring.controller.stripe;

import com.example.aadbackspring.dto.PaymentSheetRequestDTO;
import com.example.aadbackspring.model.User;
import com.example.aadbackspring.model.stripe.SubscriptionPlan;
import com.example.aadbackspring.model.stripe.UserSubscription;
import com.example.aadbackspring.repository.UserRepository;
import com.example.aadbackspring.repository.stripe.SubscriptionPlanRepository;
import com.example.aadbackspring.repository.stripe.UserSubscriptionRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.EphemeralKey;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Subscription;
import com.stripe.net.RequestOptions;
import com.stripe.param.EphemeralKeyCreateParams;

import java.time.Instant;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment-sheet")
public class PaymentSheetController {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    private static final String STRIPE_API_VERSION = "2025-02-24.acacia";

    private final UserRepository userRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public PaymentSheetController(UserRepository userRepository,
                                  UserSubscriptionRepository userSubscriptionRepository,
                                  SubscriptionPlanRepository subscriptionPlanRepository) {
        this.userRepository = userRepository;
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    @PostMapping
    public Map<String, String> createPaymentSheet(@RequestBody PaymentSheetRequestDTO request) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        // 1. Get the user
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with email: " + request.getEmail());
        }
        User user = userOpt.get();
        String stripeCustomerId = user.getStripeCustomerId();

        // 2. Create Stripe Customer if not exists
        if (stripeCustomerId == null || stripeCustomerId.isEmpty()) {
            Map<String, Object> customerParams = new HashMap<>();
            customerParams.put("email", user.getEmail());
            customerParams.put("name", user.getUsername());
            Customer customer = Customer.create(customerParams);
            stripeCustomerId = customer.getId();
            user.setStripeCustomerId(stripeCustomerId);
            userRepository.save(user);
        }

        // 3. Check if there's already an active subscription for this customer
        Optional<UserSubscription> activeSubOpt = userSubscriptionRepository.findByStripeCustomerIdAndStatus(stripeCustomerId, "active");
        if (activeSubOpt.isPresent()) {
            // ✅ Already subscribed — no need to create a new subscription
            System.out.println("User " + stripeCustomerId + " already has an active subscription: " + activeSubOpt.get().getStripeSubscriptionId());

            // Optionally still create a PaymentIntent to allow frontend to open PaymentSheet (optional)
            Map<String, String> responseData = new HashMap<>();
            responseData.put("error", "Already subscribed");
            return responseData;
        }

        // 4. Create subscription on Stripe
        Map<String, Object> subscriptionParams = new HashMap<>();
        subscriptionParams.put("customer", stripeCustomerId);

        List<Object> items = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("price", request.getPriceId());
        if (request.getQuantity() != null) {
            item.put("quantity", request.getQuantity());
        }
        items.add(item);
        subscriptionParams.put("items", items);
        subscriptionParams.put("payment_behavior", "default_incomplete");
        subscriptionParams.put("expand", Arrays.asList("latest_invoice.payment_intent"));

        Subscription subscription = Subscription.create(subscriptionParams);

        // 5. Get PaymentIntent
        Object piObject = subscription.getLatestInvoiceObject().getPaymentIntent();
        PaymentIntent paymentIntent;
        if (piObject instanceof PaymentIntent) {
            paymentIntent = (PaymentIntent) piObject;
        } else if (piObject instanceof String) {
            paymentIntent = PaymentIntent.retrieve((String) piObject);
        } else {
            throw new RuntimeException("Unexpected type for payment intent: " + piObject.getClass().getName());
        }

        // 6. Create Ephemeral Key
        EphemeralKeyCreateParams ephemeralKeyParams = EphemeralKeyCreateParams.builder()
                .setCustomer(stripeCustomerId)
                .setStripeVersion(STRIPE_API_VERSION)
                .build();
        EphemeralKey ephemeralKey = EphemeralKey.create(ephemeralKeyParams, RequestOptions.getDefault());

        // 7. Save new subscription to your database
        SubscriptionPlan plan = subscriptionPlanRepository.findByPriceId(request.getPriceId())
                .orElseThrow(() -> new RuntimeException("Subscription plan not found for priceId: " + request.getPriceId()));

        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUser(user);
        userSubscription.setSubscriptionPlan(plan);
        userSubscription.setStripeSubscriptionId(subscription.getId());
        userSubscription.setStripeCustomerId(stripeCustomerId);
        userSubscription.setStatus(subscription.getStatus());
        userSubscription.setStartDate(Instant.ofEpochSecond(subscription.getCurrentPeriodStart()));
        userSubscription.setEndDate(Instant.ofEpochSecond(subscription.getCurrentPeriodEnd()));
        userSubscriptionRepository.save(userSubscription);

        // 8. Return values to frontend
        Map<String, String> responseData = new HashMap<>();
        responseData.put("paymentIntent", paymentIntent.getClientSecret());
        responseData.put("ephemeralKey", ephemeralKey.getSecret());
        responseData.put("customer", stripeCustomerId);
        return responseData;
    }

}
