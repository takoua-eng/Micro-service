package com.esprit.microservice.club.service;

import com.esprit.microservice.club.client.UserClient;
import com.esprit.microservice.club.dto.User;
import com.esprit.microservice.club.entity.Club;
import com.esprit.microservice.club.repository.ClubRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClubService {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private UserClient userClient;

    public User getUser(String id) {
        return userClient.getUserById(id);
    }

    // 🔥 Ajouter un membre à un club
    public void addMemberToClub(Long clubId, String userId) {

        // Vérifier que le user existe dans NestJS
        User user = userClient.getUserById(userId);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Club not found"));

        club.getMembersIds().add(userId);
        clubRepository.save(club);
    }

    // 🔥 Récupérer les membres d’un club
    public List<User> getMembersOfClub(Long clubId) {

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Club not found"));

        return club.getMembersIds()
                .stream()
                .map(userClient::getUserById)
                .collect(Collectors.toList());
    }

    public Club getClubById(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club not found"));
    }

    // 🔹 Récupérer tous les clubs
    public List<Club> getAll() {
        return clubRepository.findAll();
    }

    // 🔹 Ajouter un club
    public Club addClub(Club club) {
        return clubRepository.save(club);
    }

    // 🔹 Mettre à jour un club
    public Club updateClub(Long id, Club newClub) {

        Club existingClub = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club not found"));

        existingClub.setNom(newClub.getNom());
        existingClub.setDescription(newClub.getDescription());
        existingClub.setCategorie(newClub.getCategorie());
        existingClub.setDateCreation(newClub.getDateCreation());

        return clubRepository.save(existingClub);
    }

    // 🔹 Supprimer un club
    public String deleteClub(Long id) {

        if (!clubRepository.existsById(id)) {
            return "Club non trouvé";
        }

        clubRepository.deleteById(id);
        return "Club supprimé";
    }
}