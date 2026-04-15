package tn.esprit.librairie.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.librairie.entity.Borrowing;
import tn.esprit.librairie.entity.Notification;
import tn.esprit.librairie.repository.BorrowingRepository;
import tn.esprit.librairie.repository.NotificationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepo;
    private final JavaMailSender mailSender;
    private final BorrowingRepository borrowingRepo;

    // 🔥 CORE METHOD (CORRECTED)
    public void sendNotification(String userId, String userEmail, String message, String type) {

        Notification notif = Notification.builder()
                .userId(userId)
                .userEmail(userEmail)
                .message(message)
                .type(type)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepo.save(notif);

        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(userEmail);
            mail.setSubject("Library Notification");
            mail.setText(message);

            mailSender.send(mail);

        } catch (Exception e) {
            System.out.println("EMAIL ERROR: " + e.getMessage());
        }
    }

    // ⏰ Scheduler FIXED
    @Scheduled(cron = "0 0 8 * * *")
    public void checkDueDates() {

        List<Borrowing> list = borrowingRepo.findByReturnDateIsNull();

        for (Borrowing b : list) {

            long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), b.getDueDate());

            String userId = b.getUserId();
            String userEmail = b.getUserEmail(); // ⚠️ must exist in Borrowing

            if (daysLeft == 3) {
                sendNotification(userId, userEmail,
                        "Retournez le livre dans 3 jours",
                        "REMINDER");
            }

            if (daysLeft == 1) {
                sendNotification(userId, userEmail,
                        "URGENT : retour demain !",
                        "URGENT");
            }

            if (daysLeft < 0) {
                sendNotification(userId, userEmail,
                        "Retard — amende en cours",
                        "LATE");
            }
        }
    }

    public void notifyAmende(String userId, String userEmail, double penalty) {
        sendNotification(userId, userEmail,
                "Vous avez une amende de " + penalty + " DT",
                "FINE");
    }

    public void notifyDisponible(String userId, String userEmail) {
        sendNotification(userId, userEmail,
                "Le livre réservé est maintenant disponible",
                "AVAILABLE");
    }

    public void notifyPaiement(String userId, String userEmail) {
        sendNotification(userId, userEmail,
                "Paiement effectué avec succès",
                "PAYMENT");
    }

    public void testEmail(String email) {
        sendNotification("TEST_USER", email,
                "TEST EMAIL - Spring Boot OK",
                "TEST");
    }
}