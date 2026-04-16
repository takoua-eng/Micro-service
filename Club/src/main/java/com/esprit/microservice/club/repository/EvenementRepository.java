package com.esprit.microservice.club.repository;


import com.esprit.microservice.club.entity.Club;
import com.esprit.microservice.club.entity.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Long> {

    // Tous les événements d’un club
    List<Evenement> findByClub(Club club);

    // Événements après une date
    List<Evenement> findByDateEvenementAfter(Date date);

    // Événements par club et date
    List<Evenement> findByClubAndDateEvenementAfter(Club club, Date date);
}