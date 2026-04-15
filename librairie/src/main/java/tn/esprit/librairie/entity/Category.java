package tn.esprit.librairie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "type_category_id")
    private TypeCategory typeCategory;

    // ignoré en lecture/écriture JSON pour éviter les cycles et erreurs de désérialisation
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Book> books;
}
