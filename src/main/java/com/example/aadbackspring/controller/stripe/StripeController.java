package com.example.aadbackspring.controller.stripe;

import com.example.aadbackspring.model.stripe.StripeSubscriptionResponse;
import com.example.aadbackspring.model.stripe.SubscriptionCancelRecord;
import com.example.aadbackspring.model.stripe.SubscriptionRequestDto;
import com.example.aadbackspring.service.StripeService;
import com.stripe.model.Subscription;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
public class StripeController {
    private final StripeService stripeService;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/subscription")
    public StripeSubscriptionResponse createSubscription(@RequestBody SubscriptionRequestDto subscriptionRequest) {
        return stripeService.createSubscription(subscriptionRequest);
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