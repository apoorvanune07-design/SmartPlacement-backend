package com.smartplacement.controller;

import com.smartplacement.dto.ApiResponse;
import com.smartplacement.dto.JwtResponse;
import com.smartplacement.dto.LoginRequest;
import com.smartplacement.dto.RegisterRequest;
import com.smartplacement.security.UserPrincipal;
import com.smartplacement.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success("Login successful", jwtResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<JwtResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        JwtResponse jwtResponse = authService.register(registerRequest);
        return ResponseEntity.ok(ApiResponse.success("Registration successful", jwtResponse));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Not authenticated"));
        }

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", userPrincipal.getId());
        userData.put("email", userPrincipal.getEmail());
        userData.put("fullName", userPrincipal.getFullName());
        userData.put("authorities", userPrincipal.getAuthorities());

        return ResponseEntity.ok(ApiResponse.success(userData));
    }
}
