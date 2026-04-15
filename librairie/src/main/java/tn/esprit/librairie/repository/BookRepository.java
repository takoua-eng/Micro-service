package tn.esprit.librairie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.librairie.entity.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {


    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByIsbn(String isbn);

    List<Book> findByCategoryId(Long categoryId);
}
