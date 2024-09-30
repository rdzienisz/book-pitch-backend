package com.wsb.book_pitch.controller;

import com.wsb.book_pitch.model.Booking;
import com.wsb.book_pitch.model.Pitch;
import com.wsb.book_pitch.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/objects")
    public List<Pitch> getAvailablePitches() {
        return bookingService.getAvailablePitches();
    }

    @PostMapping("/booking")
    public Booking createBooking(@RequestParam Long objectId, @RequestParam String email) {
        return bookingService.createBooking(objectId, email);
    }

    @DeleteMapping("/booking/{id}")
    public void cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
    }
}