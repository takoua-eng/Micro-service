package tn.esprit.microservice.microfoyer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.microservice.microfoyer.dto.ReservationWithUserDto;
import tn.esprit.microservice.microfoyer.dto.UserDto;
import tn.esprit.microservice.microfoyer.entity.Reservation;
import tn.esprit.microservice.microfoyer.service.reservation.IReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private IReservationService reservationService;

    @Value("${welcome.message}")
    private String welcomeMessage;

    // ==================== CRUD Reservation ====================

    @GetMapping("/all")
    public List<Reservation> getReservations() {
        return reservationService.getAll();
    }

    @GetMapping("/{reservation-id}")
    public Reservation retrieveReservation(@PathVariable("reservation-id") String id) {
        return reservationService.getById(id);
    }

    @PostMapping("/add")
    public Reservation addReservation(@RequestBody Reservation r) {
        return reservationService.add(r);
    }

    @DeleteMapping("/delete/{reservation-id}")
    public void removeReservation(@PathVariable("reservation-id") String id) {
        Reservation r = reservationService.getById(id);
        if (r != null) reservationService.delete(r);
    }

    @PutMapping("/update")
    public Reservation modifyReservation(@RequestBody Reservation r) {
        return reservationService.update(r);
    }

    // ==================== Endpoints Feign - Communication avec User MS ====================

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return reservationService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public UserDto getUserById(@PathVariable String id) {
        return reservationService.getUserById(id);
    }

    // ==================== Endpoints métier avec intégration User ====================

    /**
     * Créer une réservation pour un utilisateur spécifique.
     * POST /reservation/user/{userId}
     * Body: { "anneeUniversitaire": "2024-09-01", "estValide": true }
     */
    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createReservationForUser(
            @PathVariable String userId,
            @RequestBody Reservation reservation) {
        try {
            Reservation created = reservationService.createReservationForUser(userId, reservation);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Récupérer une réservation avec les infos complètes de l'utilisateur.
     * GET /reservation/with-user/{reservationId}
     */
    @GetMapping("/with-user/{reservationId}")
    public ResponseEntity<ReservationWithUserDto> getReservationWithUser(
            @PathVariable String reservationId) {
        ReservationWithUserDto dto = reservationService.getReservationWithUser(reservationId);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    /**
     * Récupérer toutes les réservations avec les infos des utilisateurs.
     * GET /reservation/all-with-users
     */
    @GetMapping("/all-with-users")
    public List<ReservationWithUserDto> getAllReservationsWithUsers() {
        return reservationService.getAllReservationsWithUsers();
    }

    // ==================== Config ====================

    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }
}
