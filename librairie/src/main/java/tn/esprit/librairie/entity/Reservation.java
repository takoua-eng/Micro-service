package tn.esprit.librairie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private LocalDate reservationDate;
    private String status;
    private LocalDate expirationDate;
    private int position;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonIgnore
    private Book book;
}