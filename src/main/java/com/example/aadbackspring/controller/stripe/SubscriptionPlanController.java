package com.example.aadbackspring.controller.stripe;

import com.example.aadbackspring.model.stripe.SubscriptionPlan;
import com.example.aadbackspring.repository.stripe.SubscriptionPlanRepository;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<SubscriptionPlan>> getAllPlans() {
        List<SubscriptionPlan> plans = repository.findAll();
        return ResponseEntity.ok(plans);
    }
}