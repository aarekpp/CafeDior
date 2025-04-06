package com.lemurybiznesu.backend.controller;

import com.lemurybiznesu.backend.model.dto.request.ImageRequest;
import com.lemurybiznesu.backend.model.dto.response.ImageResponse;
import com.lemurybiznesu.backend.service.MenuContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@Tag(name = "Sekcja Menu", description = "Endpointy do zarządzania Menu jako zdjęć")
public class MenuContentController {
    private final MenuContentService menuContentService;

    public MenuContentController(MenuContentService menuContentService) {
        this.menuContentService = menuContentService;
    }

    @Operation(
            summary = "Dodawanie zdjęć Menu",
            description = "Dodawanie zdjęć przez moderatora do sekcji Menu",
            security = { @SecurityRequirement(name = "accessToken") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operacja zakończona sukcesem",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Błędne dane wejściowe",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ImageResponse>> addMenuContent(
            @Parameter(description = "Obiekt żądania zawierający pliki, kolejność wyświetlania oraz sekcję")
            @ModelAttribute ImageRequest imageRequest,
            @Parameter(hidden = true, description = "Żądanie") HttpServletRequest request) {
        try{
            List<ImageResponse> responses = menuContentService.addMenuContents(imageRequest, request);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
