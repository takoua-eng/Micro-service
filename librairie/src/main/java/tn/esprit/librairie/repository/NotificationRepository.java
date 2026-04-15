package tn.esprit.librairie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.librairie.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(String userId);
}