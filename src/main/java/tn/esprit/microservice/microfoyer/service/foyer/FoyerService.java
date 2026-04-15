package tn.esprit.microservice.microfoyer.service.foyer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.microservice.microfoyer.entity.Foyer;
import tn.esprit.microservice.microfoyer.repository.IFoyerRepository;

import java.util.List;

@Service
public class FoyerService implements IFoyerService{

    @Autowired
    private IFoyerRepository foyerRepository;

    @Override
    public Foyer add(Foyer foyer) { return foyerRepository.save(foyer); }

    @Override
    public Foyer update(Foyer foyer) { return foyerRepository.save(foyer); }

    @Override
    public void delete(Foyer foyer) { foyerRepository.delete(foyer); }

    @Override
    public List<Foyer> getAll() { return foyerRepository.findAll(); }

    @Override
    public Foyer getById(Long id) {
        return foyerRepository.findById(id).orElse(null);
    }
}
