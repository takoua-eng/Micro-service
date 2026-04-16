package com.esprit.microservice.club.controller;


import com.esprit.microservice.club.dto.User;
import com.esprit.microservice.club.entity.Club;
import com.esprit.microservice.club.service.ClubService;
import org.springframework.web.bind.annotation.*;

import java.util.List;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


        import javax.sql.DataSource;


@RestController
@RequestMapping("/clubs")
public class ClubController {

    private String title = "Hello, I'm the Club Micro-Service";

    @RequestMapping("/hello")
    public String sayHello() {
        System.out.println(title);
        return title;
    }

    @Autowired
    DataSource dataSource;

    private final ClubService userClientService;

    public ClubController(ClubService userClientService) {
        this.userClientService = userClientService;
    }

    @GetMapping("/user/{userId}")
    public User getUser(@PathVariable String userId) {
        return clubService.getUser(userId);
    }

    @GetMapping("/test/db-info")
    public String getDbInfo() throws Exception {
        return dataSource.getConnection().getMetaData().getDatabaseProductName();
    }

    @Autowired
    private ClubService clubService;

    // Récupérer tous les clubs
    @GetMapping
    public ResponseEntity<List<Club>> getListClubs() {
        List<Club> clubs = clubService.getAll();
        if (clubs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(clubs);
    }

    @GetMapping("/{id}")
    public Club getClubById(@PathVariable Long id) {
        return clubService.getClubById(id);
    }

    // Ajouter un club
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Club> createClub(@RequestBody Club club) {
        return new ResponseEntity<>(clubService.addClub(club), HttpStatus.CREATED);
    }

    // Mettre à jour un club
    @PutMapping("/{id}")
    public ResponseEntity<Club> updateClub(@PathVariable("id") int id,
                                           @RequestBody Club club) {
        Club updated = clubService.updateClub((long) id, club);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // Supprimer un club
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClub(@PathVariable("id") int id) {
        return new ResponseEntity<>(clubService.deleteClub((long) id), HttpStatus.OK);
    }
}