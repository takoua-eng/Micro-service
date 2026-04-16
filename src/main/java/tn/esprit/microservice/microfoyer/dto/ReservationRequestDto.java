package tn.esprit.microservice.microfoyer.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationRequestDto {

    Date anneeUniversitaire;
    boolean estValide;
    String userId;
    Long chambreId;
}
