package tn.esprit.microservice.microfoyer.service.chambre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.microservice.microfoyer.entity.Chambre;
import tn.esprit.microservice.microfoyer.repository.IChambreRepository;

import java.util.List;
@Service
public class ChambreService implements IChambreService {

    @Autowired
    private IChambreRepository chambreRepository;

    @Override
    public Chambre add(Chambre chambre) { return chambreRepository.save(chambre); }

    @Override
    public Chambre update(Chambre chambre) { return chambreRepository.save(chambre); }

    @Override
    public void delete(Chambre chambre) { chambreRepository.delete(chambre); }

    @Override
    public List<Chambre> getAll() { return chambreRepository.findAll(); }

    @Override
    public Chambre getById(Long id) { return chambreRepository.findById(id).orElse(null); }

}
