package tn.esprit.microservice.microfoyer.service.reservation;

import tn.esprit.microservice.microfoyer.dto.ReservationWithUserDto;
import tn.esprit.microservice.microfoyer.dto.UserDto;
import tn.esprit.microservice.microfoyer.entity.Reservation;

import java.util.List;

public interface IReservationService {

    // CRUD Reservation
    Reservation add(Reservation reservation);
    Reservation update(Reservation reservation);
    void delete(Reservation reservation);
    List<Reservation> getAll();
    Reservation getById(String id);

    // Communication avec User MS via Feign
    List<UserDto> getAllUsers();
    UserDto getUserById(String id);

    // Méthodes métier avec intégration User
    Reservation createReservationForUser(String userId, Reservation reservation);
    ReservationWithUserDto getReservationWithUser(String reservationId);
    List<ReservationWithUserDto> getAllReservationsWithUsers();
}
