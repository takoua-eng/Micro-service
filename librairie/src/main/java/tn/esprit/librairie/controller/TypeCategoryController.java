package tn.esprit.librairie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.librairie.entity.TypeCategory;
import tn.esprit.librairie.service.TypeCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/type-categories")
@RequiredArgsConstructor
public class TypeCategoryController {

    private final TypeCategoryService service;

    @PostMapping
    public ResponseEntity<TypeCategory> add(@RequestBody TypeCategory typeCategory) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.add(typeCategory));
    }

    @GetMapping
    public List<TypeCategory> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public TypeCategory getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public TypeCategory update(@PathVariable Long id, @RequestBody TypeCategory typeCategory) {
        return service.update(id, typeCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
