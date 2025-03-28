package com.example.aadbackspring.service;

import com.example.aadbackspring.config.JwtTokenUtil;
import com.example.aadbackspring.config.PasswordEncoderUtil;
import com.example.aadbackspring.model.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${google.client-id}")
    private String googleClientId;

    public AuthenticationService(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public ResponseEntity<?> loginWithGoogleToken(String googleToken) {
        GoogleIdToken.Payload googlePayload;
        try {
            googlePayload = verifyGoogleToken(googleToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Invalid Google token"));
        }

        String email = googlePayload.getEmail();
        String name = (String) googlePayload.get("name");

        // Retrieve the user by email or create a new one if not found.
        Optional<User> userOptional = userService.getUserByEmail(email);
        User user = userOptional.orElseGet(() -> {
            User newUser = new User();
            String encryptedPassword = new PasswordEncoderUtil().encode("SecurePassword2025");
            newUser.setEmail(email);
            newUser.setUsername(name);
            newUser.setPassword(encryptedPassword);
            newUser.setRole("user");
            return userService.createUser(newUser);
        });

        // Create UserDetails instance for token generation.
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );

        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);
        return ResponseEntity.ok(response);
    }

    private GoogleIdToken.Payload verifyGoogleToken(String tokenString) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(tokenString);
        if (idToken != null) {
            return idToken.getPayload();
        } else {
            throw new Exception("Invalid ID token");
        }
    }
}

