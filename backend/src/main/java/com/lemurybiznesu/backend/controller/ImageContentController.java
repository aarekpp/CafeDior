package com.lemurybiznesu.backend.controller;

import com.lemurybiznesu.backend.model.dto.request.ImageUpdateRequest;
import com.lemurybiznesu.backend.model.dto.request.ImagesRequest;
import com.lemurybiznesu.backend.model.dto.response.ImageResponse;
import com.lemurybiznesu.backend.model.entity.ImageContent;
import com.lemurybiznesu.backend.service.ImageContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@Tag(name = "Zarządzanie zdjęciami", description = "Endpointy do zarządzania zdjęciami przez moderatora")
public class ImageContentController {
    private final ImageContentService imageContentService;

    public ImageContentController(ImageContentService imageContentService) {
        this.imageContentService = imageContentService;
    }

    @Operation(
            summary = "Dodawanie zdjęć",
            description = "Dodawanie zdjęć przez moderatora, gdy nie istnieją",
            security = { @SecurityRequirement(name = "accessToken") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Poprawnie dodano zdjęcia",
                    content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        array = @ArraySchema(schema = @Schema(implementation = ImageResponse.class))
            )),
            @ApiResponse(responseCode = "400", description = "Nie udało się dodać zdjęć")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addImagesContent(
            @Valid @ModelAttribute ImagesRequest imagesRequest,
            @Parameter(hidden = true, description = "Żądanie") HttpServletRequest request) {
        try{
            List<ImageContent> images = imageContentService.addImagesContent(imagesRequest, request);
            return ResponseEntity.ok(images.stream().map(ImageResponse::fromEntity).toList());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Aktualizacja zdjęcia",
            description = "Zmiana zdjęcia przez moderatora, gdy istnieje",
            security = { @SecurityRequirement(name = "accessToken") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Poprawnie zmodyfikowano zdjęcie",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ImageResponse.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Nie udało się zmodyfikować zdjęcia")
    })
    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImageContent(
            @Parameter(name = "id", description = "Identyfikator zdjęcia", example = "1") @PathVariable Long id,
            @Valid @ModelAttribute ImageUpdateRequest imageUpdateRequest,
            @Parameter(hidden = true, description = "Żądanie") HttpServletRequest request) {
        try{
            ImageContent image = imageContentService.changeImageContent(id, imageUpdateRequest, request);
            return ResponseEntity.ok(ImageResponse.fromEntity(image));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Usuwanie zdjęcia",
            description = "Usuwanie zdjęcia przez moderatora",
            security = { @SecurityRequirement(name = "accessToken") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Zdjęcie zostało usunięte"),
            @ApiResponse(responseCode = "400", description = "Nie znaleziono zdjęcia")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImageContent(
            @Parameter(description = "Identyfikator zdjęcia", example = "1") @PathVariable Long id,
            @Parameter(hidden = true) HttpServletRequest request) {

        try {
            imageContentService.deleteImageContent(id, request);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
