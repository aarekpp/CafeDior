package com.lemurybiznesu.backend.repository;

import com.lemurybiznesu.backend.model.entity.Reservation;
import com.lemurybiznesu.backend.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findAllByBookedBy(User bookedBy);
}
