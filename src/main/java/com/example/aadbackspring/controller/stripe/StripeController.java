package com.example.aadbackspring.controller.stripe;

import com.example.aadbackspring.dto.PaymentSheetRequestDTO;
import com.example.aadbackspring.model.stripe.SubscriptionCancelRecord;
import com.example.aadbackspring.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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

    @DeleteMapping("/subscription")
    public ResponseEntity<?> cancelSubscriptionByEmail(@RequestParam("email") String email) {
        try {
            Subscription subscription = stripeService.cancelSubscriptionByEmail(email);
            return ResponseEntity.ok(new SubscriptionCancelRecord(subscription.getStatus()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}