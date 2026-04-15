package tn.esprit.microservice.restaurant.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.microservice.restaurant.Entity.OrderMeal;

import java.util.List;

@Repository
public interface OrderMealRepository  extends JpaRepository<OrderMeal, Long> {

    List<OrderMeal> findByOrdersId(Long ordersId);
}
