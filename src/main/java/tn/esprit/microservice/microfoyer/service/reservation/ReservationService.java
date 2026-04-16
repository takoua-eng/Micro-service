package tn.esprit.microservice.microfoyer.service.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.microservice.microfoyer.UserClient;
import tn.esprit.microservice.microfoyer.dto.*;
import tn.esprit.microservice.microfoyer.entity.Chambre;
import tn.esprit.microservice.microfoyer.entity.Reservation;
import tn.esprit.microservice.microfoyer.repository.IChambreRepository;
import tn.esprit.microservice.microfoyer.repository.IReservationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService implements IReservationService {

    @Autowired
    private IReservationRepository reservationRepository;

    @Autowired
    private IChambreRepository chambreRepository;

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

    // ==================== Méthodes métier avec intégration User + Chambre ====================

    /**
     * Crée une réservation : un User réserve une Chambre.
     * Vérifie que l'utilisateur existe (via Feign) et que la chambre existe.
     */
    @Override
    public Reservation createReservation(ReservationRequestDto request) {
        // 1. Vérifier que l'utilisateur existe dans le microservice User
        UserDto user = userClient.getUserById(request.getUserId());
        if (user == null) {
            throw new RuntimeException("User with id " + request.getUserId() + " not found");
        }

        // 2. Vérifier que la chambre existe
        Chambre chambre = chambreRepository.findById(request.getChambreId())
                .orElseThrow(() -> new RuntimeException("Chambre with id " + request.getChambreId() + " not found"));

        // 3. Créer la réservation
        Reservation reservation = new Reservation();
        reservation.setIdReservation(UUID.randomUUID().toString());
        reservation.setAnneeUniversitaire(request.getAnneeUniversitaire());
        reservation.setEstValide(request.isEstValide());
        reservation.setUserId(request.getUserId());
        reservation.setChambre(chambre);

        return reservationRepository.save(reservation);
    }

    /**
     * Récupère une réservation avec les informations complètes (User + Chambre).
     */
    @Override
    public ReservationWithUserDto getReservationWithDetails(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);
        if (reservation == null) {
            return null;
        }
        return mapToDto(reservation);
    }

    /**
     * Récupère toutes les réservations avec les détails complets.
     */
    @Override
    public List<ReservationWithUserDto> getAllReservationsWithDetails() {
        List<Reservation> reservations = reservationRepository.findAll();
        List<ReservationWithUserDto> result = new ArrayList<>();
        for (Reservation reservation : reservations) {
            result.add(mapToDto(reservation));
        }
        return result;
    }

    /**
     * Récupère toutes les réservations d'un utilisateur spécifique.
     */
    @Override
    public List<ReservationWithUserDto> getReservationsByUserId(String userId) {
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        List<ReservationWithUserDto> result = new ArrayList<>();
        for (Reservation reservation : reservations) {
            result.add(mapToDto(reservation));
        }
        return result;
    }

    /**
     * Récupère toutes les réservations d'une chambre spécifique.
     */
    @Override
    public List<ReservationWithUserDto> getReservationsByChambreId(Long chambreId) {
        List<Reservation> reservations = reservationRepository.findByChambreIdChambre(chambreId);
        List<ReservationWithUserDto> result = new ArrayList<>();
        for (Reservation reservation : reservations) {
            result.add(mapToDto(reservation));
        }
        return result;
    }

    // ==================== Helper method ====================

    private ReservationWithUserDto mapToDto(Reservation reservation) {
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
                dto.setUser(null);
            }
        }

        // Mapper les infos de la chambre
        if (reservation.getChambre() != null) {
            Chambre chambre = reservation.getChambre();
            ChambreDto chambreDto = new ChambreDto();
            chambreDto.setIdChambre(chambre.getIdChambre());
            chambreDto.setNumeroChambre(chambre.getNumeroChambre());

            if (chambre.getBloc() != null) {
                chambreDto.setNomBloc(chambre.getBloc().getNomBloc());
                if (chambre.getBloc().getFoyer() != null) {
                    chambreDto.setNomFoyer(chambre.getBloc().getFoyer().getNomFoyer());
                }
            }
            dto.setChambre(chambreDto);
        }

        return dto;
    }
}
