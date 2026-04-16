package com.esprit.microservice.club.entity;
import jakarta.persistence.*;

import java.util.Date;

@Entity

public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private Date dateEvenement;
    private String lieu;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "club_id")
    private Club club;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateEvenement() {
        return dateEvenement;
    }

    public void setDateEvenement(Date dateEvenement) {
        this.dateEvenement = dateEvenement;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Evenement(Date dateEvenement, Club club, String lieu, String description, String titre, Long id) {
        this.dateEvenement = dateEvenement;
        this.club = club;
        this.lieu = lieu;
        this.description = description;
        this.titre = titre;
        this.id = id;
    }

    public Evenement() {
    }
}
