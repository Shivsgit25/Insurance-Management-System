package com.project.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor

@Table(name = "in_app_notifications")
public class InAppNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId; // The ID of the user this notification is for

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private boolean readStatus; // true if read, false if unread

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true) // To categorize notification types
    private String type;

    // Constructors
    public InAppNotification() {
        this.readStatus = false;
        this.createdAt = LocalDateTime.now();
    }

    public InAppNotification(String userId, String message, String type) {
        this(); // Calls the default constructor to set readStatus and createdAt
        this.userId = userId;
        this.message = message;
        this.type = type;
    }

}
