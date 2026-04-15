package tn.esprit.librairie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.librairie.entity.Reservation;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(String userId);

    List<Reservation> findByBookIdAndStatusOrderByReservationDateAsc(Long bookId, String status);
}