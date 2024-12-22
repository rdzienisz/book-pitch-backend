package com.wsb.book_pitch;

import java.time.LocalDateTime;
import lombok.Setter;

public class BookingRequest {
    @Setter
    private String email;
    private LocalDateTime startTime;
    @Setter
    private int durationHours;

    public String getEmail() {
        return email;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getDurationHours() {
        return durationHours;
    }

}