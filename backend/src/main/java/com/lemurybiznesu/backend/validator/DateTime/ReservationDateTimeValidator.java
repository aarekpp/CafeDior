package com.lemurybiznesu.backend.validator.DateTime;

import com.lemurybiznesu.backend.model.entity.Reservation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationDateTimeValidator implements ConstraintValidator<ValidReservationDateTime, Reservation> {

    @Override
    public boolean isValid(Reservation r, ConstraintValidatorContext ctx) {
        if(r.getReservationDate() == null || r.getReservationTime() == null){
            return true;
        }
        LocalDate today = LocalDate.now();
        if(r.getReservationDate().isEqual(today)){
            return !r.getReservationTime().isBefore(LocalTime.now());
        }
        return true;
    }
}
