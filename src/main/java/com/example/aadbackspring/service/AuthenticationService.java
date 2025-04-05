package com.example.aadbackspring.service;

import com.example.aadbackspring.config.JwtTokenUtil;
import com.example.aadbackspring.config.PasswordEncoderUtil;
import com.example.aadbackspring.model.User;
import com.example.aadbackspring.model.stripe.UserSubscription;
import com.example.aadbackspring.repository.UserRepository;
import com.example.aadbackspring.repository.stripe.UserSubscriptionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final PasswordEncoderUtil passwordEncoderUtil;

    public AuthenticationService(UserService userService,
                                 JwtTokenUtil jwtTokenUtil,
                                 UserRepository userRepository,
                                 UserSubscriptionRepository userSubscriptionRepository,
                                 PasswordEncoderUtil passwordEncoderUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.passwordEncoderUtil = passwordEncoderUtil;
    }

    // Validates the credentials and returns a JWT token on success.
    public ResponseEntity<?> login(String email, String password) {
        // Validate that both email and password are provided.
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Both email and password must be provided"));
        }

        Optional<User> userOptional = userService.getUserByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Invalid credentials"));
        }
        User user = userOptional.get();
        if (!passwordEncoderUtil.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Invalid credentials"));
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );

        // Optionally check for an active subscription.
        boolean hasActiveSubscription = false;
        if (user.getStripeCustomerId() != null && !user.getStripeCustomerId().isEmpty()) {
            Optional<UserSubscription> activeSub = userSubscriptionRepository
                    .findByStripeCustomerIdAndStatus(user.getStripeCustomerId(), "active");
            hasActiveSubscription = activeSub.isPresent();
        }

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("activeSubscription", hasActiveSubscription);

        String token = jwtTokenUtil.generateToken(userDetails, extraClaims);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);
        response.put("activeSubscription", hasActiveSubscription);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> refreshToken(String oldToken) {
        try {
            String email = jwtTokenUtil.getUsernameFromToken(oldToken);
            Optional<User> userOptional = userService.getUserByEmail(email);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("error", "User not found"));
            }

            User user = userOptional.get();

            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
            );

            boolean hasActiveSubscription = false;
            if (user.getStripeCustomerId() != null && !user.getStripeCustomerId().isEmpty()) {
                Optional<UserSubscription> activeSub = userSubscriptionRepository
                        .findByStripeCustomerIdAndStatus(user.getStripeCustomerId(), "active");
                hasActiveSubscription = activeSub.isPresent();
            }

            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("activeSubscription", hasActiveSubscription);
            extraClaims.put("role", userDetails.getAuthorities());

            String newToken = jwtTokenUtil.generateToken(userDetails, extraClaims);
            Map<String, Object> response = new HashMap<>();
            response.put("token", newToken);
            response.put("activeSubscription", hasActiveSubscription);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Invalid token"));
        }
    }

    // Handles the update password request.
    public ResponseEntity<?> updatePassword(String email, String currentPassword, String newPassword) {
        // Validate input parameters.
        if (email == null || email.isBlank() ||
                currentPassword == null || currentPassword.isBlank() ||
                newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Email, current password, and new password must be provided"));
        }

        Optional<User> userOptional = userService.getUserByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "User not found"));
        }
        User user = userOptional.get();

        // Verify that the provided current password matches the stored password.
        if (!passwordEncoderUtil.matches(currentPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Current password is incorrect"));
        }

        // Set the new raw password and let updateUser encode it.
        user.setPassword(newPassword);
        userService.updateUser(user.getId(), user);
        return ResponseEntity.ok(Collections.singletonMap("message", "Password updated successfully"));
    }
}
