package com.example.aadbackspring.controller.stripe;
import com.example.aadbackspring.dto.PaymentSheetRequestDTO;
import com.example.aadbackspring.model.stripe.SubscriptionCancelRecord;
import com.example.aadbackspring.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/stripe")
public class StripeController {

    private final StripeService stripeService;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping()
    public ResponseEntity<String> handleWebhook(HttpServletRequest request) {
        String sigHeader = request.getHeader("Stripe-Signature");
        String payload;
        try {
            payload = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            System.out.println("Received raw payload: " + payload);
            System.out.println("Stripe-Signature header: " + sigHeader);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Unable to read request body");
        }
        return stripeService.processWebhookEvent(payload, sigHeader);
    }

    // Payment Sheet creation endpoint
    @PostMapping("/payment-sheet")
    public Map<String, String> createPaymentSheet(@RequestBody PaymentSheetRequestDTO request) throws StripeException {
        return stripeService.createPaymentSheet(request);
    }

    @DeleteMapping("/subscription/{id}")
    public SubscriptionCancelRecord cancelSubscription(@PathVariable("id") String subscriptionId) {
        Subscription subscription = stripeService.cancelSubscription(subscriptionId);
        if (subscription != null) {
            return new SubscriptionCancelRecord(subscription.getStatus());
        }
        return null;
    }
}