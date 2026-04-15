package tn.esprit.librairie.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.librairie.entity.Category;
import tn.esprit.librairie.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public Category addCategory(Category category) {
        return repository.save(category);
    }

    public List<Category> getAllCategories() {
        return repository.findAll();
    }

    public Category getCategory(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category updateCategory(Long id, Category updated) {
        Category existing = getCategory(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setTypeCategory(updated.getTypeCategory());
        return repository.save(existing);
    }

    public void deleteCategory(Long id) {
        repository.deleteById(id);
    }

    public List<Category> getByTypeId(Long typeCategoryId) {
        return repository.findByTypeCategoryId(typeCategoryId);
    }
}
