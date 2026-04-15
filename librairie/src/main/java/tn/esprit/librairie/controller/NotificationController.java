package tn.esprit.librairie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.librairie.entity.Notification;
import tn.esprit.librairie.repository.NotificationRepository;
import tn.esprit.librairie.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepo;
    private final NotificationService notificationService;

    // 📋 ALL NOTIFICATIONS
    @GetMapping
    public List<Notification> getAll() {
        return notificationRepo.findAll();
    }

    // 👤 BY USER
    @GetMapping("/user/{userId}")
    public List<Notification> getByUser(@PathVariable String userId) {
        return notificationRepo.findByUserId(userId);
    }

    // 🔥 TEST EMAIL (IMPORTANT)
    @GetMapping("/test-email/{email}")
    public String testEmail(@PathVariable String email) {
        notificationService.testEmail(email);
        return "Email test sent to " + email;
    }
}