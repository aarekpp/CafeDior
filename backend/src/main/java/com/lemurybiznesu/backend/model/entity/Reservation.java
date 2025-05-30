package com.lemurybiznesu.backend.model.entity;

import com.lemurybiznesu.backend.validator.DateTime.ValidReservationDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservations")
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ValidReservationDateTime
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    @EqualsAndHashCode.Include
    private UUID id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status", nullable = false, columnDefinition = "VARCHAR(10)")
    private EReservationStatus reservationStatus = EReservationStatus.ACTIVE;

    @NotNull
    @FutureOrPresent
    @Column(name = "reservation_date", nullable = false, columnDefinition = "DATE")
    private LocalDate reservationDate;

    @NotNull
    @Column(name = "reservation_time",nullable = false, columnDefinition = "TIME")
    private LocalTime reservationTime;

    @NotNull
    @Min(1)
    @Max(10)
    @Column(nullable = false, length = 10)
    private Integer people;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booked_by", referencedColumnName = "id", columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    private User bookedBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}
