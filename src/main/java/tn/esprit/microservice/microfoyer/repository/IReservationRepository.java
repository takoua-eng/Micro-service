package tn.esprit.microservice.microfoyer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.microservice.microfoyer.entity.Reservation;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation, String> {
}
