package com.example.aadbackspring.controller;

import com.example.aadbackspring.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        return authenticationService.login(email, password);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String currentPassword = payload.get("currentPassword");
        String newPassword = payload.get("newPassword");
        return authenticationService.updatePassword(email, currentPassword, newPassword);
    }
}
