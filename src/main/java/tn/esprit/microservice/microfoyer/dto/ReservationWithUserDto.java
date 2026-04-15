package tn.esprit.microservice.microfoyer.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationWithUserDto {

    String idReservation;
    Date anneeUniversitaire;
    boolean estValide;

    // Infos de l'utilisateur récupérées via Feign
    UserDto user;
}
