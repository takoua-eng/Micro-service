package tn.esprit.librairie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.librairie.entity.Book;
import tn.esprit.librairie.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    // ➕ Ajouter
    @PostMapping
    public Book add(@RequestBody Book book) {
        return service.addBook(book);
    }

    // 📋 Tous
    @GetMapping
    public List<Book> getAll() {
        return service.getAllBooks();
    }

    // 🔍 Par ID
    @GetMapping("/{id}")
    public Book getById(@PathVariable Long id) {
        return service.getBook(id);
    }

    // ✏️ Update CORRECT
    @PutMapping("/{id}")
    public Book update(@PathVariable Long id,
                       @RequestBody Book book) {
        return service.updateBook(id, book);
    }

    // ❌ Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteBook(id);
    }

    // 🔥 SEARCH GLOBAL (IMPORTANT)
    @GetMapping("/search")
    public List<Book> search(@RequestParam String keyword) {
        return service.search(keyword);
    }

    // 🔎 filtres optionnels
    @GetMapping("/search/author")
    public List<Book> searchByAuthor(@RequestParam String author) {
        return service.searchByAuthor(author);
    }

    @GetMapping("/search/category/{categoryId}")
    public List<Book> searchByCategory(@PathVariable Long categoryId) {
        return service.searchByCategory(categoryId);
    }
}