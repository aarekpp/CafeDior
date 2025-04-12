package com.lemurybiznesu.backend.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImagesRequest {
    @NotEmpty
    @Schema(description = "Lista plików graficznych")
    private List<MultipartFile> files;

    @NotEmpty
    @Schema(description = "Lista sekcji przypisanych do plików (kolejność musi odpowiadać plikom)",
            allowableValues = {"MENU1", "MENU2", "CONTACT1", "CONTACT2"})
    private List<String> sections;
}
