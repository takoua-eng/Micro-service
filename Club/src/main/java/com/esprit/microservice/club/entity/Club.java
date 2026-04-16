package com.esprit.microservice.club.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;
    private String categorie;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateCreation;

    // 🔥 AJOUT POUR OPENFEIGN (members)
    @ElementCollection
    private Set<String> membersIds = new HashSet<>();

    public Club() {
        super();
    }

    public Club(String nom) {
        this.nom = nom;
    }

    public Club(Long id, String nom, String description, String categorie, Date dateCreation) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.categorie = categorie;
        this.dateCreation = dateCreation;
    }

    // getters & setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Set<String> getMembersIds() {
        return membersIds;
    }

    public void setMembersIds(Set<String> membersIds) {
        this.membersIds = membersIds;
    }
}