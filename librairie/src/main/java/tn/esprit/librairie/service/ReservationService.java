package tn.esprit.librairie.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.librairie.entity.Book;
import tn.esprit.librairie.entity.Reservation;
import tn.esprit.librairie.repository.BookRepository;
import tn.esprit.librairie.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepo;
    private final BookRepository bookRepo;
    private final NotificationService notificationService;
    private final UserClient userClient;

    // ------------------ EMAIL ------------------
    private String getUserEmail(String userId) {
        try {
            UserDto user = userClient.getUserById(userId);
            return user.getEmail();
        } catch (Exception e) {
            return userId.contains("@") ? userId : userId + "@library.com";
        }
    }

    // ------------------ RESERVATION ------------------
    public Reservation reserveBook(Long bookId, String userId) {

        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getAvailableCopies() > 0) {
            throw new RuntimeException("Book is available → borrow directly");
        }

        List<Reservation> queue =
                reservationRepo.findByBookIdAndStatusOrderByReservationDateAsc(bookId, "PENDING");

        Reservation reservation = Reservation.builder()
                .book(book) // ✔ FIXED
                .userId(userId)
                .reservationDate(LocalDate.now())
                .status("PENDING")
                .expirationDate(null)
                .position(queue.size() + 1)
                .build();

        return reservationRepo.save(reservation);
    }

    // ------------------ CANCEL ------------------
    public void cancelReservation(Long id) {

        Reservation r = reservationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        r.setStatus("CANCELLED");
        reservationRepo.save(r);
    }

    // ------------------ GET ------------------
    public List<Reservation> getReservationsByUser(String userId) {
        return reservationRepo.findByUserId(userId);
    }

    public List<Reservation> getAll() {
        return reservationRepo.findAll();
    }

    // ------------------ AUTO EXPIRE ------------------
    @Scheduled(cron = "0 0 * * * *")
    public void handleExpiredReservations() {

        List<Reservation> list = reservationRepo.findAll();

        for (Reservation r : list) {

            if (r.getExpirationDate() != null &&
                    r.getExpirationDate().isBefore(LocalDate.now()) &&
                    "PENDING".equals(r.getStatus())) {

                r.setStatus("EXPIRED");
                reservationRepo.save(r);

                Book book = r.getBook();

                List<Reservation> queue =
                        reservationRepo.findByBookIdAndStatusOrderByReservationDateAsc(book.getId(), "PENDING");

                if (!queue.isEmpty()) {

                    Reservation next = queue.get(0);
                    next.setExpirationDate(LocalDate.now().plusDays(1));
                    reservationRepo.save(next);
                    String userId = next.getUserId();
                    String email = getUserEmail(next.getUserId());

                    notificationService.sendNotification(
                            userId,
                            email,
                            "C’est votre tour ! Vous avez 24h",
                            "AVAILABLE"
                    );
                }
            }
        }
    }
}