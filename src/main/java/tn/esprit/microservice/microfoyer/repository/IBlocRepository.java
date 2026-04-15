package tn.esprit.microservice.microfoyer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.microservice.microfoyer.entity.Bloc;

@Repository
public interface IBlocRepository extends JpaRepository<Bloc, Long> {
}
