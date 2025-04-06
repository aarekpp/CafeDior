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
@Schema(description = "Zwraca linki do zdjęć Menu znajdujących się na serwerze")
public class ImageResponse {
    @Schema(description = "Unikalny identyfikator zdjęcia", example = "507f1f77bcf86cd799439011")
    private String id;
    @Schema(description = "Ścieżka do zdjęcia", example = "https://www.lemurybiznesu.com/images/menu/abcd.png")
    private String url;
    @Schema(description = "Kolejność wyswietlania zdjęcia na stronie", example = "1")
    private Integer displayOrder;
}
