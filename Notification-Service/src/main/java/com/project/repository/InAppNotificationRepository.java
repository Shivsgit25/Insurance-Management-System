package com.project.repository;

import com.project.model.InAppNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InAppNotificationRepository extends JpaRepository<InAppNotification, Long> {
    // Custom query to find notifications for a specific user, ordered by creation time (descending)
    List<InAppNotification> findByUserIdOrderByCreatedAtDesc(String userId);

    // Optional: find unread notifications for a user
    List<InAppNotification> findByUserIdAndReadStatusFalseOrderByCreatedAtDesc(String userId);
}
