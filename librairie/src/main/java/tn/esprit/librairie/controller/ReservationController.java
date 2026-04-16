package tn.esprit.librairie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.librairie.entity.Reservation;
import tn.esprit.librairie.entity.DTO.ReservationRequest;
import tn.esprit.librairie.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService service;

    @PostMapping
    public Reservation reserve(@RequestBody ReservationRequest request) {
        return service.reserveBook(request.getBookId(), request.getUserId());
    }

    @DeleteMapping("/{id}")
    public void cancel(@PathVariable Long id) {
        service.cancelReservation(id);
    }

    @GetMapping("/user/{userId}")
    public List<Reservation> getByUser(@PathVariable String userId) {
        return service.getReservationsByUser(userId);
    }

    @GetMapping
    public List<Reservation> getAll() {
        return service.getAll();
    }
}