package com.lemurybiznesu.backend.controller;

import com.lemurybiznesu.backend.model.dto.request.ImageRequest;
import com.lemurybiznesu.backend.model.dto.response.ImageResponse;
import com.lemurybiznesu.backend.service.ContactContentService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@Tag(name = "Sekcja Kontakt", description = "Endpointy do zarządzania sekcją Kontakt jako zdjęć")
public class ContactContentController {
    private final ContactContentService contactContentService;

    public ContactContentController(ContactContentService contactContentService) {
        this.contactContentService = contactContentService;
    }

    @Operation(
            summary = "Dodawanie zdjęć z kontaktem",
            description = "Dodawanie zdjęć kontaktowych przez moderatora",
            security = { @SecurityRequirement(name = "accessToken") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operacja zakończona sukcesem",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Błędne dane wejściowe")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ImageResponse>> addContactContent(
            @Parameter(description = "Obiekt żądania zawierający pliki, kolejność wyświetlania oraz sekcję")
            @ModelAttribute ImageRequest imageRequest,
            @Parameter(hidden = true) HttpServletRequest request) {
        try{
            List<ImageResponse> responses = contactContentService.addContactContents(imageRequest, request);
            return ResponseEntity.ok(responses);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
