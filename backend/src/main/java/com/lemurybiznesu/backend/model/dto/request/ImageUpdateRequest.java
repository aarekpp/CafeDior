package com.lemurybiznesu.backend.model.dto.request;

import com.lemurybiznesu.backend.model.entity.EImageContent;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Żądanie aktualizacji pojedynczego zdjęcia")
public class ImageUpdateRequest {
    @NotNull
    @Schema(
            description = "Plik graficzny do aktualizacji",
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            format = "binary"
    )
    private MultipartFile file;

    @NotNull
    @Schema(
            description = "Typ sekcji zdjęcia",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "MENU1",
            allowableValues = {"MENU1", "MENU2", "CONTACT1", "CONTACT2"}
    )
    private EImageContent type;
}
