package tn.esprit.librairie.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.librairie.entity.Book;
import tn.esprit.librairie.entity.Category;
import tn.esprit.librairie.repository.BookRepository;
import tn.esprit.librairie.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    // 📋 All
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // ➕ Add
    public Book addBook(Book book) {

        if (book.getTotalCopies() < 0) {
            throw new RuntimeException("Total copies invalid");
        }

        book.setAvailableCopies(book.getTotalCopies());

        // 🔥 FIX CATEGORY
        if (book.getCategory() != null && book.getCategory().getId() != null) {

            Category category = categoryRepository.findById(book.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            book.setCategory(category); // 🔥 important
        }

        return bookRepository.save(book);
    }


    // 🔍 Get by id
    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
    }

    // ✏️ Update FIXED
    public Book updateBook(Long id, Book updatedBook) {

        Book existing = getBook(id);

        existing.setTitle(updatedBook.getTitle());
        existing.setAuthor(updatedBook.getAuthor());
        existing.setIsbn(updatedBook.getIsbn());
        existing.setTotalCopies(updatedBook.getTotalCopies());

        // ⚠️ update disponibilité intelligemment
        if (updatedBook.getTotalCopies() >= existing.getAvailableCopies()) {
            existing.setAvailableCopies(updatedBook.getTotalCopies());
        }

        return bookRepository.save(existing);
    }

    // ❌ delete
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    // 🔥 SEARCH GLOBAL
    public List<Book> search(String keyword) {
        return bookRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public List<Book> searchByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    public List<Book> searchByCategory(Long categoryId) {
        return bookRepository.findByCategoryId(categoryId);
    }
}