package com.esprit.microservice.club.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "membre")
public class Membre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String role; // PRESIDENT, MEMBRE, SECRETAIRE, TRESORIER

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    // ✅ Constructeur complet
    public Membre(Long id, Long userId, String role, Club club) {
        this.id = id;
        this.userId = userId;
        this.role = role;
        this.club = club;
    }

    // ✅ Constructeur vide obligatoire JPA
    public Membre() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Club getClub() { return club; }
    public void setClub(Club club) { this.club = club; }
}