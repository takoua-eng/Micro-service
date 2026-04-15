package tn.esprit.microservice.microfoyer.service.chambre;

import tn.esprit.microservice.microfoyer.entity.Chambre;

import java.util.List;

public interface IChambreService {

    Chambre add(Chambre chambre);
    Chambre update(Chambre chambre);
    void delete(Chambre chambre);
    List<Chambre> getAll();
    Chambre getById(Long id);
}
