package com.wsb.book_pitch.controller;

import com.wsb.book_pitch.model.Booking;
import com.wsb.book_pitch.model.Pitch;
import com.wsb.book_pitch.service.BookingService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/pitches")
    public List<Pitch> getAvailablePitches() {
        return bookingService.getAvailablePitches();
    }

    @PostMapping("/booking")
    public Booking createBooking(@RequestParam Long objectId,
            @RequestParam String email,
            @RequestParam LocalDateTime startTime,
            @RequestParam int durationHours) {
     return bookingService.createBooking(objectId, email, startTime, durationHours);
}

    @DeleteMapping("/booking/{id}")
    public void cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
    }

    @GetMapping("/bookings/{pitchId}")
    public List<Booking> getBookingsByPitch(@PathVariable Long pitchId) {
        return bookingService.getBookingsByPitch(pitchId);
    }

    @GetMapping("/availability/{pitchId}")
    public ResponseEntity<List<LocalTime>> checkAvailability(
            @PathVariable Long pitchId,
            @RequestParam String date) {
        List<LocalTime> availableSlots = bookingService.checkAvailability(pitchId, LocalDate.parse(date));
        return ResponseEntity.ok(availableSlots);
    }
}