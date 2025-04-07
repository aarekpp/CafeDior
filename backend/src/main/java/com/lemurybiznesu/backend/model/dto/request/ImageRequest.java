package com.lemurybiznesu.backend.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Przesłane zdjęcia")
public class ImageRequest {
    @NotEmpty
    @Schema(description = "Pliki")
    private List<MultipartFile> files;
    @NotEmpty
    @Schema(description = "Kolejność wyświetlania zdjęć", example = "[2,1]")
    private List<Integer> displayOrders;
    @NotBlank
    @Schema(description = "Sekcja do której należą zdjęcia (menu, contact)", example = "menu")
    private String section;
}
