package com.lemurybiznesu.backend.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Dane wymagane do rejestracji użytkownika")
public class SignupRequest {
    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^\\p{L}+$", message = "First name must contain only letters")
    @Schema(description = "Imię użytkownika (tylko litery)", example = "Jan", minLength = 1, maxLength = 50)
    private String firstName;

    @NotBlank @Size(max = 80)
    @Pattern(
            regexp = "^[\\p{L}\\s-]+$",
            message = "Last name must contain only letters, spaces or hyphens"
    )
    @Schema(description = "Nazwisko użytkownika (litery, spacje i myślniki)", example = "Kowalski-Nowak", minLength = 1, maxLength = 80)
    private String lastName;

    @NotBlank @Email
    @Schema(description = "Unikalny adres email", example = "jan.kowalski@example.com")
    private String email;

    @NotBlank @Size(min = 1, max = 15)
    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Invalid phone number format")
    @Schema(description = "Numer telefonu w formacie międzynarodowym lub lokalnym (opcjonalny znak + na początku)",
            example = "+48123456789", minLength = 1, maxLength = 15)
    private String phoneNumber;

    @NotBlank @Size(min = 10, max = 30)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,30}$",
            message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character (@$!%*?&)"
    )
    @Schema(description = "Hasło (wymagane: 1 mała litera, 1 wielka litera, 1 cyfra, 1 znak specjalny @$!%*?&)",
            example = "ZAQ!2wsxcd", minLength = 10, maxLength = 30)
    private String password;

    @NotBlank @Size(min = 10, max = 30)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,30}$")
    @Schema(description = "Potwierdzenie hasła (musi być identyczne jak pole password)",
            example = "ZAQ!2wsxcd", minLength = 10, maxLength = 30)
    private String confirmPassword;

    @AssertTrue(message = "Passwords do not match")
    private boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}
