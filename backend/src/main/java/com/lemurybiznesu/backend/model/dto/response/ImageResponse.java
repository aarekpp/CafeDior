package com.lemurybiznesu.backend.model.dto.response;

import com.lemurybiznesu.backend.model.entity.ImageContent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Zawiera informacje o zdjęciu przechowywanym na serwerze")
public class ImageResponse {
    @Schema(
            description = "Unikalny identyfikator zdjęcia w bazie danych",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Typ zdjęcia aplikacji",
            example = "MENU1",
            allowableValues = {"MENU1", "MENU2", "CONTACT1", "CONTACT2"}
    )
    private String type;

    @Schema(
            description = "Publiczny URL do zdjęcia na serwerze",
            example = "/image/550e8400-e29b-41d4-a716-446655440000.jpg"
    )
    private String url;

    public static ImageResponse fromEntity(ImageContent imageContent) {
        return new ImageResponse(
          imageContent.getId(),
          imageContent.getImageContentType().name(),
          "/image/" + imageContent.getFilename() + "?v=" + imageContent.getVersion()
        );
    }
}
