package com.example.aadbackspring.controller.stripe;

import com.example.aadbackspring.model.stripe.SubscriptionPlan;
import com.example.aadbackspring.repository.stripe.SubscriptionPlanRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionPlanController {

    private final SubscriptionPlanRepository repository;

    public SubscriptionPlanController(SubscriptionPlanRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<SubscriptionPlan> getAllPlans() {
        List<SubscriptionPlan> plans = repository.findAll();
        if (plans.isEmpty()) {
            System.out.println("⚠️ No Subscription Plans found in DB!");
        } else {
            System.out.println("✅ Found " + plans.size() + " subscription plans.");
        }
        return plans;
    }
}