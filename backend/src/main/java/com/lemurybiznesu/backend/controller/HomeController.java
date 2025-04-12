package com.lemurybiznesu.backend.controller;

import com.lemurybiznesu.backend.model.dto.response.HomeDataResponse;
import com.lemurybiznesu.backend.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
@Tag(name = "Strona główna", description = "Pobieranie wszystkich danych dla strony głównej")
public class HomeController {
    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @Operation(
            summary = "Pobieranie danych dla strony głównej",
            description = "Możliwość pobrania danych przez każdego"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Poprawnie pobrano dane", content = @Content(schema = @Schema(implementation = HomeDataResponse.class))),
            @ApiResponse(responseCode = "400", description = "Nie udało się pobrać danych")
    })
    @GetMapping
    public ResponseEntity<?> getHomeData() {
        try{
            HomeDataResponse homeDataResponse = homeService.getHomeData();
            return ResponseEntity.ok(homeDataResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
