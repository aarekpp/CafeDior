package com.lemurybiznesu.backend.service;

import com.lemurybiznesu.backend.model.dto.request.ReservationRequest;
import com.lemurybiznesu.backend.model.entity.Reservation;
import com.lemurybiznesu.backend.model.entity.User;
import com.lemurybiznesu.backend.repository.ReservationRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final AuthService authService;

    public ReservationService(ReservationRepository reservationRepository, AuthService authService) {
        this.reservationRepository = reservationRepository;
        this.authService = authService;
    }

    public List<Reservation> getAllReservations(HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);
        return reservationRepository.findAllByBookedBy(currentUser);
    }

    @Transactional
    public Reservation createReservation(HttpServletRequest request, ReservationRequest reservation) {
        User currentUser = authService.getCurrentUser(request);
        Reservation createdReservation = new Reservation();
        createdReservation.setReservationDate(reservation.getReservationDate());
        createdReservation.setReservationTime(reservation.getReservationTime());
        createdReservation.setPeople(reservation.getPeople());
        createdReservation.setBookedBy(currentUser);

        try{
            return reservationRepository.save(createdReservation);
        } catch (Exception e) {
            return null;
        }
    }
}
