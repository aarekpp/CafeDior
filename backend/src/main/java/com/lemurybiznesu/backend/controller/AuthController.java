package com.lemurybiznesu.backend.controller;

import com.lemurybiznesu.backend.model.dto.request.SigninRequest;
import com.lemurybiznesu.backend.model.dto.response.AuthResponse;
import com.lemurybiznesu.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody SigninRequest signinRequest) {
        AuthResponse authResponse = authService.authenticateUser(request, response, signinRequest);

        if (authResponse != null) {
            return ResponseEntity.ok().body(authResponse);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/verify-token")
    public ResponseEntity<?> verifyToken() {
        AuthResponse authResponse = authService.verifyToken();
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
        authService.logout(request, response);
        return ResponseEntity.ok().body("Logout successful");
    }
}
