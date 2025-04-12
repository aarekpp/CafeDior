package com.lemurybiznesu.backend.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Zawiera dane takie jak opis sekcji 'O nas' i linki do zdjęć na serwerze")
public class HomeDataResponse {
    @Schema(description = "Tekst sekcji 'O nas'", example = "Przykładowy tekst opisujący kawiarnię.")
    private String aboutContent;

    @Schema(
            description = "Lista zdjęć przynależących do konkretnych sekcji",
            example = "[{id: 1,type: MENU1,url: /api/image/550e8400-e29b-41d4-a716-446655440000.jpg}]")
    private List<ImageResponse> images;
}
