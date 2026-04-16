package tn.esprit.microservice.restaurant.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.criteria.Order;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderMeal {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    private Double price;


    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Orders orders;


    @ManyToOne
    @JoinColumn(name = "meal_id")
    private Meal meal;







}
