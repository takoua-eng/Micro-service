package com.esprit.microservice.club.service;
import com.esprit.microservice.club.entity.Club;
import com.esprit.microservice.club.repository.ClubRepository;
import com.esprit.microservice.club.entity.Evenement;
import com.esprit.microservice.club.repository.EvenementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvenementService {

    private final EvenementRepository evenementRepository;
    private final ClubRepository clubRepository;

    public EvenementService(EvenementRepository evenementRepository,
                            ClubRepository clubRepository) {
        this.evenementRepository = evenementRepository;
        this.clubRepository = clubRepository;
    }

    // ✅ CREATE
    public Evenement create(Evenement e) {

        if (e.getClub() == null || e.getClub().getId() == null) {
            throw new RuntimeException("Club ID obligatoire");
        }

        Long clubId = e.getClub().getId();

        // ✅ récupérer le vrai club depuis la base
        Club clubFromDb = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Club introuvable"));

        // 🔥 force le chargement (important pour éviter null)
        clubFromDb.getNom();

        // 🔥 remplacer le club envoyé par celui de la DB
        e.setClub(clubFromDb);

        // ✅ sauvegarde
        return evenementRepository.save(e);
    }


    // ✅ GET ALL
    public List<Evenement> getAll() {
        return evenementRepository.findAll();
    }

    // ✅ GET BY ID (AJOUT IMPORTANT)
    public Evenement getById(Long id) {
        Optional<Evenement> opt = evenementRepository.findById(id);
        return opt.orElse(null);
    }

    // ✅ DELETE (sécurisé)
    public void delete(Long id) {
        if (evenementRepository.existsById(id)) {
            evenementRepository.deleteById(id);
        } else {
            throw new RuntimeException("Evenement introuvable avec id = " + id);
        }
    }

    // ✅ GET BY CLUB (VERSION SIMPLE)
    public List<Evenement> getByClubId(Long clubId) {
        return evenementRepository.findAll()
                .stream()
                .filter(e -> e.getClub() != null && e.getClub().getId().equals(clubId))
                .toList();
    }
}