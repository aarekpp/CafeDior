package com.lemurybiznesu.backend.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignupRequest {
    @NotBlank @Size(max = 50)
    @Pattern(regexp = "^\\p{L}+$", message = "First name must contain only letters")
    private String firstName;

    @NotBlank @Size(max = 80)
    @Pattern(
            regexp = "^[\\p{L}\\s-]+$",
            message = "Last name must contain only letters, spaces or hyphens"
    )
    private String lastName;

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 10, max = 30)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,30}$",
            message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character (@$!%*?&)"
    )
    private String password;

    @NotBlank @Size(min = 10, max = 30)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,30}$",
            message = "Confirm password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character (@$!%*?&)"
    )
    private String confirmPassword;

    @NotBlank @Size(max = 20)
    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Invalid phone number format")
    private String phoneNumber;

    @AssertTrue(message = "Passwords do not match")
    private boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}
