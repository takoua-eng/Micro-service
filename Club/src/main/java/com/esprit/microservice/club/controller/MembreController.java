package com.esprit.microservice.club.controller;

import com.esprit.microservice.club.entity.Membre;
import com.esprit.microservice.club.service.MembreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/membres")
@CrossOrigin("*")
public class MembreController {

    private final MembreService membreService;

    public MembreController(MembreService membreService) {
        this.membreService = membreService;
    }

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<?> add(@RequestBody Membre m) {
        try {
            Membre saved = membreService.addMembre(m);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<List<Membre>> getAll() {
        return ResponseEntity.ok(membreService.getAll());
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Membre> getById(@PathVariable Long id) {
        Membre membre = membreService.getById(id);

        if (membre == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(membre);
    }

    // ✅ GET BY CLUB
    @GetMapping("/club/{clubId}")
    public ResponseEntity<List<Membre>> getByClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(membreService.getByClubId(clubId));
    }

    // ✅ GET BY USER
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Membre>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(membreService.getByUserId(userId));
    }

    // ✅ GET BY ROLE
    @GetMapping("/role/{role}")
    public ResponseEntity<List<Membre>> getByRole(@PathVariable String role) {
        return ResponseEntity.ok(membreService.getByRole(role));
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody Membre m) {

        Membre existing = membreService.getById(id);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            m.setId(id);
            return ResponseEntity.ok(membreService.addMembre(m));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        Membre existing = membreService.getById(id);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        membreService.delete(id);

        return ResponseEntity.noContent().build();
    }
}