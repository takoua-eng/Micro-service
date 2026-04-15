package tn.esprit.librairie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.librairie.entity.TypeCategory;

import java.util.Optional;

@Repository
public interface TypeCategoryRepository extends JpaRepository<TypeCategory, Long> {
    Optional<TypeCategory> findByName(String name);
}
