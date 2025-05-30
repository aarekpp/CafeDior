package com.lemurybiznesu.backend.validator.DateTime;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;

@Documented
@Constraint(validatedBy = ReservationDateTimeValidator.class)
public @interface ValidReservationDateTime {
    String message() default "Godzina rezerwacji dla dzisiejszego dnia nie może być wcześniejsza niż teraz";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
