package com.lemurybiznesu.backend.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Odpowiedź autentykacyjna zawierająca informacje o użytkowniku")
public class AuthResponse {
    @Schema(description = "Unikalny identyfikator użytkownika", example = "507f1f77bcf86cd799439011")
    private String userId;

    @Schema(
            description = "Rola użytkownika w systemie",
            example = "ROLE_USER",
            allowableValues = {"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER"}
    )
    private String role;
}
