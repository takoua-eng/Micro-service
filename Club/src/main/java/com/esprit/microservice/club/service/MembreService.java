package com.esprit.microservice.club.service;

import com.esprit.microservice.club.entity.Club;
import com.esprit.microservice.club.entity.Membre;
import com.esprit.microservice.club.repository.ClubRepository;
import com.esprit.microservice.club.repository.MembreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembreService {

    private final MembreRepository membreRepository;
    private final ClubRepository clubRepository;

    public MembreService(MembreRepository membreRepository,
                         ClubRepository clubRepository) {
        this.membreRepository = membreRepository;
        this.clubRepository = clubRepository;
    }

    // ✅ Ajouter un membre
    public Membre addMembre(Membre m) {

        if (m.getClub() == null || m.getClub().getId() == null) {
            throw new RuntimeException("Club ID obligatoire");
        }

        Club club = clubRepository.findById(m.getClub().getId())
                .orElseThrow(() -> new RuntimeException("Club introuvable avec ID = " + m.getClub().getId()));

        m.setClub(club);

        return membreRepository.save(m);
    }

    // ✅ Récupérer tous les membres
    public List<Membre> getAll() {
        return membreRepository.findAll();
    }

    // ✅ Récupérer un membre par ID
    public Membre getById(Long id) {
        return membreRepository.findById(id).orElse(null);
    }

    // ✅ Récupérer les membres d'un club
    public List<Membre> getByClubId(Long clubId) {
        return membreRepository.findByClubId(clubId);
    }

    // ✅ Récupérer les clubs d'un utilisateur
    public List<Membre> getByUserId(Long userId) {
        return membreRepository.findByUserId(userId);
    }

    // ✅ Récupérer par rôle
    public List<Membre> getByRole(String role) {
        return membreRepository.findByRole(role);
    }

    // ✅ Supprimer un membre
    public void delete(Long id) {
        membreRepository.deleteById(id);
    }
}