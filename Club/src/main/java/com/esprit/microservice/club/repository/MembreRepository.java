package com.esprit.microservice.club.repository;

import com.esprit.microservice.club.entity.Membre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembreRepository extends JpaRepository<Membre, Long> {

    // ✅ Trouver tous les membres d'un club
    List<Membre> findByClubId(Long clubId);

    // ✅ Trouver tous les clubs d'un utilisateur
    List<Membre> findByUserId(Long userId);

    // ✅ Trouver par rôle
    List<Membre> findByRole(String role);

    // ✅ Vérifier si un user est déjà membre d'un club
    boolean existsByUserIdAndClubId(Long userId, Long clubId);
}