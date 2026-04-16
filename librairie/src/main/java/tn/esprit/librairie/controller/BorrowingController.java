package tn.esprit.librairie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.librairie.entity.Borrowing;
import tn.esprit.librairie.entity.DTO.BorrowRequest;
import tn.esprit.librairie.service.BorrowingService;

import java.util.List;

@RestController
@RequestMapping("/api/borrowings")
@RequiredArgsConstructor
public class BorrowingController {

    private final BorrowingService service;

    // 🔥 Emprunter un livre (clean API)
    @PostMapping
    public ResponseEntity<Borrowing> borrow(@RequestBody BorrowRequest request) {
        Borrowing borrowing = service.borrowBook(request.getBookId(), request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(borrowing);
    }

    // 🔥 Retourner un livre
    @PutMapping("/return/{borrowingId}")
    public ResponseEntity<Borrowing> returnBook(@PathVariable Long borrowingId) {
        Borrowing borrowing = service.returnBook(borrowingId);
        return ResponseEntity.ok(borrowing);
    }

    // 👤 Mes emprunts
    @GetMapping("/user/{userId}")
    public List<Borrowing> getByUser(@PathVariable String userId) {
        return service.getBorrowingsByUser(userId);
    }

    // 📌 actifs
    @GetMapping("/active")
    public List<Borrowing> active() {
        return service.getActiveBorrowings();
    }

    // ⚠️ en retard
    @GetMapping("/late")
    public List<Borrowing> late() {
        return service.getLateBorrowings();
    }
    @GetMapping
    public List<Borrowing> getAll() {
        return service.getAllBorrowings();
    }
}