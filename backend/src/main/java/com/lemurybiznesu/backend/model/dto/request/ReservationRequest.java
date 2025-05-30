package com.lemurybiznesu.backend.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@Schema(description = "Dane wymagane do złożenia rezerwacji")
public class ReservationRequest {
    @NotNull
    @FutureOrPresent
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Schema(description = "Data rezerwacji", example = "20/10/2025")
    private LocalDate reservationDate;

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    @Schema(description = "Godzina rezerwacji", example = "15:00")
    private LocalTime reservationTime;

    @NotNull
    @Min(1)
    @Max(10)
    @Schema(description = "Liczba osób podana w rezerwacji", example = "4")
    private Integer people;
}
