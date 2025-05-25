package com.lemurybiznesu.backend.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dane do aktualizacji użytkownika")
public class UserDetailsUpdateRequest {
    @NotBlank
    @Schema(description = "Imię", example = "Jan")
    private String firstName;
    @NotBlank
    @Schema(description = "Nazwisko", example = "Kowalski")
    private String lastName;
    @NotBlank
    @Schema(description = "Numer telefonu", example = "+48123456789")
    private String phoneNumber;
}
