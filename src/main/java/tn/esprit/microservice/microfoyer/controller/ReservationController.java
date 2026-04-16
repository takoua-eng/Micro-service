package tn.esprit.microservice.microfoyer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.microservice.microfoyer.dto.ReservationRequestDto;
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

    // ==================== Endpoints métier : User réserve une Chambre ====================

    /**
     * Créer une réservation : Un User réserve une Chambre.
     *
     * POST /reservation/create
     * Body: {
     *   "userId": "680f2a3b5c7d8e9f12345678",
     *   "chambreId": 1,
     *   "anneeUniversitaire": "2024-09-01",
     *   "estValide": true
     * }
     */
    @PostMapping("/create")
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequestDto request) {
        try {
            Reservation created = reservationService.createReservation(request);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Récupérer une réservation avec tous les détails (User + Chambre).
     * GET /reservation/details/{reservationId}
     */
    @GetMapping("/details/{reservationId}")
    public ResponseEntity<ReservationWithUserDto> getReservationDetails(@PathVariable String reservationId) {
        ReservationWithUserDto dto = reservationService.getReservationWithDetails(reservationId);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    /**
     * Récupérer toutes les réservations avec tous les détails.
     * GET /reservation/all-details
     */
    @GetMapping("/all-details")
    public List<ReservationWithUserDto> getAllReservationsWithDetails() {
        return reservationService.getAllReservationsWithDetails();
    }

    /**
     * Récupérer toutes les réservations d'un utilisateur.
     * GET /reservation/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public List<ReservationWithUserDto> getReservationsByUser(@PathVariable String userId) {
        return reservationService.getReservationsByUserId(userId);
    }

    /**
     * Récupérer toutes les réservations d'une chambre.
     * GET /reservation/chambre/{chambreId}
     */
    @GetMapping("/chambre/{chambreId}")
    public List<ReservationWithUserDto> getReservationsByChambre(@PathVariable Long chambreId) {
        return reservationService.getReservationsByChambreId(chambreId);
    }

    // ==================== Config ====================

    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }
}
