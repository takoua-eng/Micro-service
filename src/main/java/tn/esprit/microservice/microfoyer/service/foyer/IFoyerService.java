package tn.esprit.microservice.microfoyer.service.foyer;

import tn.esprit.microservice.microfoyer.entity.Foyer;

import java.util.List;

public interface IFoyerService {

    Foyer add(Foyer foyer);
    Foyer update(Foyer foyer);
    void delete(Foyer foyer);
    List<Foyer> getAll();
    Foyer getById(Long id);
}
