package tn.esprit.microservice.restaurant.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.microservice.restaurant.Entity.Orders;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUserId(String userId);
    List<Orders> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
