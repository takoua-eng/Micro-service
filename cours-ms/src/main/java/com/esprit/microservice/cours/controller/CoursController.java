package com.esprit.microservice.cours.controller;

import com.esprit.microservice.cours.dto.UserDTO;
import com.esprit.microservice.cours.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cours")
public class CoursController {

    private final CourseService courseService;

    public CoursController(CourseService courseService) {
        this.courseService = courseService;
    }

    // --- OpenFeign user-ms ---

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(courseService.getUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        return ResponseEntity.ok(courseService.getUserById(id));
    }

    // --- Favorites scenario ---

    @PostMapping("/{id}/favorite-users/{userId}")
    public ResponseEntity<Void> saveFavoriteUser(
            @PathVariable int id,
            @PathVariable int userId) {
        courseService.saveFavoriteUser(id, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/favorite-users")
    public ResponseEntity<List<UserDTO>> getFavoriteUsers(@PathVariable int id) {
        return ResponseEntity.ok(courseService.getFavoriteUsers(id));
    }
}

