package com.lemurybiznesu.backend.controller;

import com.lemurybiznesu.backend.model.dto.request.ReservationRequest;
import com.lemurybiznesu.backend.model.dto.response.ReservationResponse;
import com.lemurybiznesu.backend.model.entity.Reservation;
import com.lemurybiznesu.backend.service.ReservationService;
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
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Rezerwacje", description = "Endpointy dla rezerwacji użytkownika")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(
            summary = "Pobieranie rezerwacji",
            description = "Pobieranie rezerwacji przez konkretnego użytkownika",
            security = { @SecurityRequirement(name = "accessToken") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Poprawnie pobrano rezerwacje",   content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = ReservationResponse.class)
                    )
            )),
            @ApiResponse(responseCode = "400", description = "Nie udało się pobrać rezerwacji")
    })
    @GetMapping
    public ResponseEntity<?> getAllReservations(HttpServletRequest request) {
        List<Reservation> reservations = reservationService.getAllReservations(request);
        return ResponseEntity.ok(reservations.stream().map(ReservationResponse::fromEntity).toList());
    }

    @Operation(
            summary = "Dodawanie rezerwacji",
            description = "Dodawanie rezerwacji przez użytkownika",
            security = { @SecurityRequirement(name = "accessToken") }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Poprawnie dodano rezerwację", content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Nie udało się zapisać rezerwacji")
    })
    @PostMapping
    public ResponseEntity<?> createReservation(
            @Parameter(hidden = true, description = "Żądanie") HttpServletRequest request, @Valid @RequestBody ReservationRequest reservation) {
        Reservation createdReservation = reservationService.createReservation(request, reservation);
        if(createdReservation != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(ReservationResponse.fromEntity(createdReservation));
        }else{
            return ResponseEntity.badRequest().body(null);
        }
    }
}
