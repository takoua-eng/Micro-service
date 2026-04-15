package tn.esprit.librairie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.librairie.entity.*;
import tn.esprit.librairie.repository.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BorrowingService {

    private final BorrowingRepository borrowingRepo;
    private final BookRepository bookRepo;
    private final ReservationRepository reservationRepo;
    private final UserClient userClient;
    private final NotificationService notificationService;

    public Borrowing borrowBook(Long bookId, String userId) {

        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No available copies");
        }

        // essayer de récupérer l'email via Feign, sinon utiliser userId comme fallback
        String userEmail = getUserEmail(userId);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepo.save(book);

        Borrowing borrowing = Borrowing.builder()
                .book(book)
                .userId(userId)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .returnDate(null)
                .penalty(0.0)
                .build();

        Borrowing saved = borrowingRepo.save(borrowing);

        // envoyer notification avec l'email récupéré
        notificationService.sendNotification(
                userId,
                userEmail,
                "Vous avez emprunté le livre: " + book.getTitle()
                        + ". Date de retour: " + saved.getDueDate(),
                "BORROW"
        );

        return saved;
    }

    public Borrowing returnBook(Long borrowingId) {

        Borrowing borrowing = borrowingRepo.findById(borrowingId)
                .orElseThrow(() -> new RuntimeException("Borrowing not found"));

        borrowing.setReturnDate(LocalDate.now());

        Book book = borrowing.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepo.save(book);

        // 🔥 queue كاملة
        List<Reservation> queue =
                reservationRepo.findByBookIdAndStatusOrderByReservationDateAsc(book.getId(), "PENDING");

        if (!queue.isEmpty()) {

            Reservation first = queue.get(0);

            // 🔥 إعطاء 24h
            first.setExpirationDate(LocalDate.now().plusDays(1));
            reservationRepo.save(first);

            String firstUserId = first.getUserId();
            String firstEmail = getUserEmail(firstUserId);

            notificationService.sendNotification(
                    firstUserId,
                    firstEmail,
                    "Livre disponible, vous avez 24h pour l'emprunter",
                    "AVAILABLE"
            );

            // 🔥 باقي users (position)
            for (int i = 1; i < queue.size(); i++) {

                Reservation r = queue.get(i);
                String userId = r.getUserId();
                String email = getUserEmail(userId);

                notificationService.sendNotification(
                        userId,
                        email,
                        "Vous êtes en position " + (i + 1),
                        "INFO"
                );
            }
        }

        return borrowingRepo.save(borrowing);
    }

    // récupère l'email via Feign — si user-service down, utilise userId comme fallback
    private String getUserEmail(String userId) {
        try {
            UserDto user = userClient.getUserById(userId);
            if (user != null && user.getEmail() != null && !user.getEmail().isBlank()) {
                return user.getEmail();
            }
        } catch (Exception e) {
            log.warn("user-service unavailable, using userId as email fallback: {}", userId);
        }
        // fallback: si userId ressemble à un email, l'utiliser directement
        return userId.contains("@") ? userId : userId + "@library.com";
    }

    public List<Borrowing> getActiveBorrowings() {
        return borrowingRepo.findByReturnDateIsNull();
    }

    public List<Borrowing> getLateBorrowings() {
        return borrowingRepo.findByDueDateBeforeAndReturnDateIsNull(LocalDate.now());
    }

    public List<Borrowing> getBorrowingsByUser(String userId) {
        return borrowingRepo.findByUserId(userId);
    }

    private boolean isAllowedRole(String role) {
        return role != null &&
                (role.equalsIgnoreCase("STUDENT")
                        || role.equalsIgnoreCase("TEACHER")
                        || role.equalsIgnoreCase("MEMBER"));
    }

    public List<Borrowing> getAllBorrowings() {
        return borrowingRepo.findAll();
    }
}
