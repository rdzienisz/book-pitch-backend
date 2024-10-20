package com.wsb.book_pitch.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Pitch pitch;

    private String userEmail;
    private LocalDateTime startTime; // Start time of the booking
    private LocalDateTime endTime; // End time of the bookin
    private boolean isActive;

    public void setIsActive(boolean b) {
        isActive = b;
    }
}
