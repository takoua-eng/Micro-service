package tn.esprit.librairie.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.librairie.entity.Book;
import tn.esprit.librairie.entity.Borrowing;
import tn.esprit.librairie.entity.DTO.DashboardResponse;
import tn.esprit.librairie.repository.BookRepository;
import tn.esprit.librairie.repository.BorrowingRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final BookRepository bookRepo;
    private final BorrowingRepository borrowingRepo;

    public DashboardResponse getDashboard() {

        long totalBooks = bookRepo.count();

        long availableBooks = bookRepo.findAll()
                .stream()
                .mapToLong(Book::getAvailableCopies)
                .sum();

        long borrowedBooks = bookRepo.findAll()
                .stream()
                .mapToLong(b -> b.getTotalCopies() - b.getAvailableCopies())
                .sum();

        long totalBorrowings = borrowingRepo.count();

        long activeBorrowings = borrowingRepo.findByReturnDateIsNull().size();

        long lateBorrowings = borrowingRepo
                .findByDueDateBeforeAndReturnDateIsNull(LocalDate.now())
                .size();

        double totalPenalties = borrowingRepo.findAll()
                .stream()
                .mapToDouble(Borrowing::getPenalty)
                .sum();

        return DashboardResponse.builder()
                .totalBooks(totalBooks)
                .availableBooks(availableBooks)
                .borrowedBooks(borrowedBooks)
                .totalBorrowings(totalBorrowings)
                .activeBorrowings(activeBorrowings)
                .lateBorrowings(lateBorrowings)
                .totalPenalties(totalPenalties)
                .build();
    }
}
