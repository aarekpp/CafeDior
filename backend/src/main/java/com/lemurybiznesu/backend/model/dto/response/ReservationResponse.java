package com.lemurybiznesu.backend.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lemurybiznesu.backend.model.entity.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Zwracane dane rezerwacji")
public class ReservationResponse {
    @Schema(description = "Unikalne ID rezerwacji", example = "507f1f77bcf86cd799439011")
    private String id;
    @Schema(description = "Status rezerwacji", example = "ACTIVE")
    private String status;
    @Schema(description = "Data rezerwacji", example = "20/10/2025")
    private LocalDate reservationDate;
    @JsonFormat(pattern = "HH:mm")
    @Schema(description = "Godzina rezerwacji", example = "15:00")
    private LocalTime reservationTime;
    @Schema(description = "Liczba osób podana w rezerwacji", example = "4")
    private Integer people;

    public static ReservationResponse fromEntity(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId().toString(),
                reservation.getReservationStatus().toString(),
                reservation.getReservationDate(),
                reservation.getReservationTime(),
                reservation.getPeople()
        );
    }
}