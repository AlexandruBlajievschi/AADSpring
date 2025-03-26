package com.example.aadbackspring.controller.stripe;

import com.example.aadbackspring.model.stripe.UserSubscription;
import com.example.aadbackspring.repository.stripe.UserSubscriptionRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.Subscription;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/webhooks/stripe")
public class StripeWebhookController {

    // Inject the webhook secret from your properties file.
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    private final UserSubscriptionRepository userSubscriptionRepository;

    public StripeWebhookController(UserSubscriptionRepository userSubscriptionRepository) {
        this.userSubscriptionRepository = userSubscriptionRepository;
    }

    @PostMapping
    public ResponseEntity<String> handleStripeEvent(HttpServletRequest request) {
        String sigHeader = request.getHeader("Stripe-Signature");
        String payload;

        // Read the raw request body without modification.
        try {
            payload = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            System.out.println("Received raw payload: " + payload);
            System.out.println("Stripe-Signature header: " + sigHeader);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Unable to read request body");
        }

        Event event;
        try {
            // Construct the event using the raw payload, signature header, and endpoint secret.
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            System.out.println("📩 Stripe webhook received: " + event.getType());
        } catch (SignatureVerificationException e) {
            System.err.println("Webhook signature verification failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Webhook signature verification failed.");
        } catch (Exception e) {
            System.err.println("Error constructing event: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error processing event.");
        }

        // Handle events of interest.
        switch (event.getType()) {
            case "invoice.payment_succeeded": {
                // For invoices that are paid, mark the corresponding subscription as active.
                Invoice invoice = (Invoice) event.getDataObjectDeserializer().getObject().orElse(null);
                if (invoice != null && invoice.getSubscription() != null) {
                    String subscriptionId = invoice.getSubscription();
                    Optional<UserSubscription> optSubscription =
                            userSubscriptionRepository.findByStripeSubscriptionId(subscriptionId);
                    if (optSubscription.isPresent()) {
                        UserSubscription userSub = optSubscription.get();
                        userSub.setStatus("active");
                        userSub.setStartDate(Instant.ofEpochSecond(invoice.getPeriodStart()));
                        userSub.setEndDate(Instant.ofEpochSecond(invoice.getPeriodEnd()));
                        userSubscriptionRepository.save(userSub);
                        System.out.println("Updated subscription " + subscriptionId + " to active");
                    } else {
                        System.out.println("No matching subscription record found for subscription ID: " + subscriptionId);
                    }
                }
                break;
            }
            case "customer.subscription.updated": {
                // When a subscription is updated (e.g. canceled, paused, or resumed), update the record.
                Subscription subscription = (Subscription) event.getDataObjectDeserializer().getObject().orElse(null);
                if (subscription != null) {
                    String subscriptionId = subscription.getId();
                    Optional<UserSubscription> optSubscription =
                            userSubscriptionRepository.findByStripeSubscriptionId(subscriptionId);
                    if (optSubscription.isPresent()) {
                        UserSubscription userSub = optSubscription.get();
                        userSub.setStatus(subscription.getStatus());
                        userSub.setStartDate(Instant.ofEpochSecond(subscription.getCurrentPeriodStart()));
                        userSub.setEndDate(Instant.ofEpochSecond(subscription.getCurrentPeriodEnd()));
                        userSubscriptionRepository.save(userSub);
                        System.out.println("Updated subscription " + subscriptionId + " status to " + subscription.getStatus());
                    } else {
                        System.out.println("No matching subscription record found for subscription ID: " + subscriptionId);
                    }
                }
                break;
            }
            // Add additional event types as needed.
            default:
                System.out.println("Unhandled event type: " + event.getType());
                break;
        }

        // Return a 200 response to acknowledge receipt of the event.
        return ResponseEntity.ok("Success");
    }
}
