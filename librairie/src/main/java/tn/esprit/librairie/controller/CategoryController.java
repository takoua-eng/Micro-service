package tn.esprit.librairie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.librairie.entity.Category;
import tn.esprit.librairie.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<Category> add(@RequestBody Category category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addCategory(category));
    }

    @GetMapping
    public List<Category> getAll() {
        return service.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category getById(@PathVariable Long id) {
        return service.getCategory(id);
    }

    @PutMapping("/{id}")
    public Category update(@PathVariable Long id, @RequestBody Category category) {
        return service.updateCategory(id, category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // Filtrer les catégories par type
    @GetMapping("/type/{typeCategoryId}")
    public List<Category> getByType(@PathVariable Long typeCategoryId) {
        return service.getByTypeId(typeCategoryId);
    }
}
