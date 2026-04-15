package tn.esprit.librairie.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.librairie.entity.TypeCategory;
import tn.esprit.librairie.repository.TypeCategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeCategoryService {

    private final TypeCategoryRepository repository;

    public TypeCategory add(TypeCategory typeCategory) {
        return repository.save(typeCategory);
    }

    public List<TypeCategory> getAll() {
        return repository.findAll();
    }

    public TypeCategory getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TypeCategory not found"));
    }

    public TypeCategory update(Long id, TypeCategory updated) {
        TypeCategory existing = getById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
