package tn.esprit.microservice.microfoyer.service.bloc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.microservice.microfoyer.entity.Bloc;
import tn.esprit.microservice.microfoyer.repository.IBlocRepository;

import java.util.List;

@Service
public class BlocService implements IBlocService {

    @Autowired
    IBlocRepository blocRepository;

    @Override
    public Bloc add(Bloc bloc){
        return blocRepository.save(bloc);
    }

    @Override
    public Bloc update(Bloc bloc){
        return blocRepository.save(bloc);
    }

    @Override
    public void delete(Bloc bloc){
        blocRepository.delete(bloc);
    }

    @Override
    public List<Bloc> getAll(){ return blocRepository.findAll(); }

    @Override
    public Bloc getById(Long id){
        return blocRepository.findById(id).orElse(null);
    }

}
