package com.lemurybiznesu.backend.controller;

import com.lemurybiznesu.backend.model.dto.request.AboutContentRequest;
import com.lemurybiznesu.backend.model.entity.AboutContent;
import com.lemurybiznesu.backend.service.AboutContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/about")
@Tag(name = "Sekcja informacji 'O nas'", description = "Endpointy dla zarządzania treścią sekcji 'O nas'")
public class AboutContentController {
    private final AboutContentService aboutContentService;

    public AboutContentController(AboutContentService aboutContentService) {
        this.aboutContentService = aboutContentService;
    }

    @Operation(
            summary = "Pobieranie tekstu",
            description = "Możliwość pobrania tekstu przez każdego"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Poprawnie pobrano tekst", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Nie udało się pobrać tekstu")
    })
    @GetMapping
    public ResponseEntity<?> getAboutContent() {
        Optional<AboutContent> content = aboutContentService.getAboutContent();
        if (content.isPresent()) {
            return ResponseEntity.ok(content.get().getText());
        }else{
            return ResponseEntity.ok().body(null);
        }
    }

    @Operation(
            summary = "Dodawanie tekstu",
            description = "Dodawanie tekstu przez moderatora, gdy nie istnieje",
            security = { @SecurityRequirement(name = "accessToken") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Poprawnie dodano tekst", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Nie udało się dodać tekstu")
    })
    @PostMapping
    public ResponseEntity<?> createAboutContent(
            @Valid @RequestBody AboutContentRequest aboutContentRequest,
            @Parameter(hidden = true, description = "Żądanie") HttpServletRequest request) {
        AboutContent content = aboutContentService.createAboutContent(aboutContentRequest, request);
        return ResponseEntity.ok().body(content.getText());
    }

    @Operation(
            summary = "Aktualizacja tekstu",
            description = "Zmiana tekstu przez moderatora",
            security = { @SecurityRequirement(name = "accessToken") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Poprawnie zaktualizowano tekst", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Nie udało się zaktualizować tekstu")
    })
    @PutMapping
    public ResponseEntity<?> updateAboutContent(
            @Valid @RequestBody AboutContentRequest aboutContentRequest,
            @Parameter(hidden = true, description = "Żądanie") HttpServletRequest request) {
        AboutContent content = aboutContentService.updateAboutContent(aboutContentRequest, request);
        return ResponseEntity.ok().body(content.getText());
    }

    @Operation(
            summary = "Usuwanie tekstu",
            description = "Usuwanie tekstu przez moderatora",
            security = { @SecurityRequirement(name = "accessToken") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usunięto tekst", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "Nie udało się usunąć tekstu")
    })
    @DeleteMapping
    public ResponseEntity<?> deleteAboutContent(@Parameter(hidden = true, description = "Żądanie") HttpServletRequest request) {
        boolean deleted = aboutContentService.deleteAboutContent(request);
        return ResponseEntity.ok().body(deleted);
    }
}
