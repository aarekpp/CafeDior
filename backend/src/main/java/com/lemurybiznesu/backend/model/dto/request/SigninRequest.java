package com.lemurybiznesu.backend.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Dane wymagane do logowania")
public class SigninRequest {
    @NotBlank
    @Email
    @Schema(description = "Adres email", example = "user@example.com")
    String email;

    @NotBlank
    @Size(min = 10, max = 30)
    @Schema(description = "Hasło", example = "strongpassword123", minLength = 10, maxLength = 30)
    String password;
}
