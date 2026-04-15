package tn.esprit.microservice.microfoyer.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Chambre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idChambre;

    public Long numeroChambre;

    @ManyToOne
    private Bloc bloc;

    @ManyToMany
    private List<Reservation> reservations;
}
