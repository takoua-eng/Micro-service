package tn.esprit.microservice.microfoyer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.microservice.microfoyer.entity.Reservation;

import java.util.List;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation, String> {

    List<Reservation> findByUserId(String userId);

    List<Reservation> findByChambreIdChambre(Long chambreId);
}
