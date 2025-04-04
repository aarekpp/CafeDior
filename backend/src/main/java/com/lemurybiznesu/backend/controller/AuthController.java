package com.lemurybiznesu.backend.controller;

import com.lemurybiznesu.backend.model.dto.request.SigninRequest;
import com.lemurybiznesu.backend.model.dto.request.SignupRequest;
import com.lemurybiznesu.backend.model.dto.response.AuthResponse;
import com.lemurybiznesu.backend.model.dto.response.UserResponse;
import com.lemurybiznesu.backend.model.entity.User;
import com.lemurybiznesu.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Uwierzytelnianie", description = "Endpointy do zarządzania uwierzytelnianiem")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Logowanie użytkownika",
            description = "Uwierzytelnia użytkownika, zwraca tokeny w ciasteczkach i wymagane dane"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logowanie udane", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane wejściowe")
    })
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(
            @Parameter(hidden = true, description = "Zapytanie") HttpServletRequest request,
            @Parameter(hidden = true, description = "Odpowiedź") HttpServletResponse response,
            @Valid @RequestBody SigninRequest signinRequest) {
        AuthResponse authResponse = authService.authenticateUser(request, response, signinRequest);

        if (authResponse != null) {
            return ResponseEntity.ok().body(authResponse);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(
            summary = "Weryfikacja tokenu",
            description = "Sprawdza ważność tokenów podczas ładowania aplikacji i zwraca potrzebne dane"
    )
    @SecurityRequirement(name = "accessToken")
    @ApiResponse(responseCode = "200", description = "Token poprawny", content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    @PostMapping("/verify-token")
    public ResponseEntity<?> verifyToken() {
        AuthResponse authResponse = authService.verifyToken();
        return ResponseEntity.ok(authResponse);
    }

    @Operation(
            summary = "Wylogowanie",
            description = "Unieważnia tokeny i czyści ciasteczka"
    )
    @SecurityRequirement(name = "accessToken")
    @ApiResponse(responseCode = "200", description = "Wylogowano pomyślnie")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @Parameter(hidden = true, description = "Zapytanie") HttpServletRequest request,
            @Parameter(hidden = true, description = "Odpowiedź") HttpServletResponse response){
        authService.logout(request, response);
        return ResponseEntity.ok().body("Logout successful");
    }

    @Operation(
            summary = "Rejestracja użytkownika",
            description = "Tworzy konto nowego użytkownika"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Zarejestrowano nowego użytkownika", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane wejściowe")
    })
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {
        User newUser = authService.signupUser(signupRequest);
        return ResponseEntity.ok().body(UserResponse.fromEntity(newUser));
    }
}
