package com.wsb.book_pitch.controller;

import com.wsb.book_pitch.BookingRequest;
import com.wsb.book_pitch.model.Booking;
import com.wsb.book_pitch.model.Pitch;
import com.wsb.book_pitch.service.BookingService;
import com.wsb.book_pitch.util.TimeSlot;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/bookings/{pitchId}")
    public Booking createBooking(@PathVariable Long pitchId,
            @RequestBody BookingRequest bookingRequest) throws IOException {

        String email = bookingRequest.getEmail();
        LocalDateTime startTime = bookingRequest.getStartTime();
        int durationHours = bookingRequest.getDurationHours();

        return bookingService.createBooking(pitchId, email, startTime, durationHours);
    }

    @GetMapping("/bookings/{pitchId}")
    public List<Booking> getBookingsByPitch(@PathVariable Long pitchId) {
        return bookingService.getBookingsByPitch(pitchId);
    }

    @GetMapping("/availability/{pitchId}")
    public ResponseEntity<List<TimeSlot>> checkAvailability(
            @PathVariable Long pitchId,
            @RequestParam String date) {
        List<TimeSlot> availableSlots = bookingService.checkAvailability(pitchId,
                LocalDate.parse(date));
        return ResponseEntity.ok(availableSlots);
    }
}