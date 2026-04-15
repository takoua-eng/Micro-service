package tn.esprit.microservice.microfoyer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.microservice.microfoyer.entity.Foyer;

@Repository
public interface IFoyerRepository extends JpaRepository<Foyer,Long> {
}
