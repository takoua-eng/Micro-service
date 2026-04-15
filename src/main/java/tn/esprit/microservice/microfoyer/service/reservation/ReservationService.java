package tn.esprit.microservice.microfoyer.service.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.microservice.microfoyer.UserClient;
import tn.esprit.microservice.microfoyer.dto.ReservationWithUserDto;
import tn.esprit.microservice.microfoyer.dto.UserDto;
import tn.esprit.microservice.microfoyer.entity.Reservation;
import tn.esprit.microservice.microfoyer.repository.IReservationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService implements IReservationService {

    @Autowired
    private IReservationRepository reservationRepository;

    @Autowired
    private UserClient userClient;

    // ==================== CRUD de base ====================

    @Override
    public Reservation add(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation update(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public void delete(Reservation reservation) {
        reservationRepository.delete(reservation);
    }

    @Override
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getById(String id) {
        return reservationRepository.findById(id).orElse(null);
    }

    // ==================== Communication Feign avec User MS ====================

    @Override
    public List<UserDto> getAllUsers() {
        return userClient.getAllUsers();
    }

    @Override
    public UserDto getUserById(String id) {
        return userClient.getUserById(id);
    }

    // ==================== Méthodes métier avec intégration User ====================

    /**
     * Crée une réservation pour un utilisateur spécifique.
     * Vérifie d'abord que l'utilisateur existe via Feign.
     */
    @Override
    public Reservation createReservationForUser(String userId, Reservation reservation) {
        // Vérifier que l'utilisateur existe dans le microservice User
        UserDto user = userClient.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User with id " + userId + " not found");
        }

        // Générer un ID de réservation si non fourni
        if (reservation.getIdReservation() == null || reservation.getIdReservation().isEmpty()) {
            reservation.setIdReservation(UUID.randomUUID().toString());
        }

        // Associer l'utilisateur à la réservation
        reservation.setUserId(userId);

        return reservationRepository.save(reservation);
    }

    /**
     * Récupère une réservation avec les informations complètes de l'utilisateur.
     */
    @Override
    public ReservationWithUserDto getReservationWithUser(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);
        if (reservation == null) {
            return null;
        }

        ReservationWithUserDto dto = new ReservationWithUserDto();
        dto.setIdReservation(reservation.getIdReservation());
        dto.setAnneeUniversitaire(reservation.getAnneeUniversitaire());
        dto.setEstValide(reservation.isEstValide());

        // Récupérer les infos de l'utilisateur via Feign
        if (reservation.getUserId() != null) {
            try {
                UserDto user = userClient.getUserById(reservation.getUserId());
                dto.setUser(user);
            } catch (Exception e) {
                // Si le user n'existe plus, on laisse null
                dto.setUser(null);
            }
        }

        return dto;
    }

    /**
     * Récupère toutes les réservations avec les informations des utilisateurs.
     */
    @Override
    public List<ReservationWithUserDto> getAllReservationsWithUsers() {
        List<Reservation> reservations = reservationRepository.findAll();
        List<ReservationWithUserDto> result = new ArrayList<>();

        for (Reservation reservation : reservations) {
            ReservationWithUserDto dto = new ReservationWithUserDto();
            dto.setIdReservation(reservation.getIdReservation());
            dto.setAnneeUniversitaire(reservation.getAnneeUniversitaire());
            dto.setEstValide(reservation.isEstValide());

            if (reservation.getUserId() != null) {
                try {
                    UserDto user = userClient.getUserById(reservation.getUserId());
                    dto.setUser(user);
                } catch (Exception e) {
                    dto.setUser(null);
                }
            }

            result.add(dto);
        }

        return result;
    }
}
