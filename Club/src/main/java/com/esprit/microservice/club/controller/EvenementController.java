package com.esprit.microservice.club.controller;

import com.esprit.microservice.club.entity.Evenement;
import com.esprit.microservice.club.service.EvenementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evenements")
@CrossOrigin(origins = "*")
public class EvenementController {

    private final EvenementService evenementService;

    public EvenementController(EvenementService evenementService) {
        this.evenementService = evenementService;
    }

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<Evenement> create(@RequestBody Evenement e) {
        Evenement saved = evenementService.create(e);
        return ResponseEntity.ok(saved);
    }

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<List<Evenement>> getAll() {
        List<Evenement> list = evenementService.getAll();
        return ResponseEntity.ok(list);
    }

    // ✅ GET BY ID (ajout important)
    @GetMapping("/{id}")
    public ResponseEntity<Evenement> getById(@PathVariable Long id) {
        Evenement evenement = evenementService.getById(id);
        if (evenement == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(evenement);
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Evenement> update(@PathVariable Long id, @RequestBody Evenement e) {
        Evenement existing = evenementService.getById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        e.setId(id);
        Evenement updated = evenementService.create(e);
        return ResponseEntity.ok(updated);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        evenementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ GET BY CLUB (corrigé)
    @GetMapping("/club/{clubId}")
    public ResponseEntity<List<Evenement>> getByClub(@PathVariable Long clubId) {
        List<Evenement> list = evenementService.getByClubId(clubId);
        return ResponseEntity.ok(list);
    }
}