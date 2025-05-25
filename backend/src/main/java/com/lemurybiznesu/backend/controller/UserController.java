package com.lemurybiznesu.backend.controller;

import com.lemurybiznesu.backend.model.dto.request.UserDetailsUpdateRequest;
import com.lemurybiznesu.backend.model.dto.response.UserDetailsResponse;
import com.lemurybiznesu.backend.model.entity.User;
import com.lemurybiznesu.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "Obsługa danych użytkownika", description = "Endpointy do pobierania oraz modyfikacji danych użytkowników")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Pobieranie danych użytkownika",
            description = "Pobiera dane użytkownika potrzebne do panelu edycji danych konta"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pobrano dane", content = @Content(schema = @Schema(implementation = UserDetailsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Nie ma użytkownika o wskazanym ID")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserData(@PathVariable String userId, @Parameter(hidden = true, description = "Zapytanie") HttpServletRequest request) {
        User user = userService.getUser(userId, request);
        if(user != null) {
            return ResponseEntity.ok(UserDetailsResponse.fromEntity(user));
        }else{
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(
            summary = "Aktualizacja danych",
            description = "Aktualizauje dane użytkownika"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Zaktualizowano dane", content = @Content(schema = @Schema(implementation = UserDetailsUpdateRequest.class))),
            @ApiResponse(responseCode = "400", description = "Nie udało się zaktualizować danych użytkownika")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, HttpServletRequest request, @Valid @RequestBody UserDetailsUpdateRequest updateData) {
        User updatedUser = userService.updateUserDetails(userId, request, updateData);
        if(updatedUser != null) {
            return ResponseEntity.ok(UserDetailsResponse.fromEntity(updatedUser));
        }else{
            return ResponseEntity.badRequest().body(null);
        }
    }
}
