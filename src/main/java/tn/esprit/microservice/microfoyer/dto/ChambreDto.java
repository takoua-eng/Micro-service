package tn.esprit.microservice.microfoyer.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChambreDto {

    Long idChambre;
    Long numeroChambre;
    String nomBloc;      // Nom du bloc où se trouve la chambre
    String nomFoyer;     // Nom du foyer
}
