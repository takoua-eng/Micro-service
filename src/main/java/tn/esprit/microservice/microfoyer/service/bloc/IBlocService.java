package tn.esprit.microservice.microfoyer.service.bloc;

import tn.esprit.microservice.microfoyer.entity.Bloc;

import java.util.List;

public interface IBlocService {

    Bloc add(Bloc bloc);
    Bloc update(Bloc bloc);
    void delete(Bloc bloc);
    List<Bloc> getAll();
    Bloc getById(Long id);
}
