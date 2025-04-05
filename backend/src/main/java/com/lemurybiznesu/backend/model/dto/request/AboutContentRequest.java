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
@Schema(description = "Tekst sekcji 'O nas'")
public class AboutContentRequest {
    @NotBlank
    @Schema(description = "Tekst", example = "Przykładowy tekst sekcji, w której opisywana jest kawiarnia")
    private String text;
}
