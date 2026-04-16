package tn.esprit.microservice.microfoyer.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reservation {

    @Id
    String idReservation;

    @Temporal(TemporalType.DATE)
    Date anneeUniversitaire;

    boolean estValide;

    // Référence vers l'utilisateur du microservice User (stocke juste l'ID)
    String userId;

    // Relation avec la Chambre réservée
    @ManyToOne
    @JoinColumn(name = "chambre_id")
    Chambre chambre;
}
