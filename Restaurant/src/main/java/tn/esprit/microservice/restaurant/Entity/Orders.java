package tn.esprit.microservice.restaurant.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // userId stored as String to match User microservice identifiers
    private String userId;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // PENDING, PAID, CANCELLED

    private LocalDateTime createdAt;



    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<OrderMeal> orderMeals;


}
