package com.wsb.book_pitch.controller;

import com.wsb.book_pitch.model.Availability;
import com.wsb.book_pitch.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/availability")
    public Availability createAvailability(@RequestBody Availability availability) {
        return bookingService.createAvailability(availability);
    }

    @DeleteMapping("/availability/{id}")
    public void deleteAvailability(@PathVariable Long id) {
        bookingService.deleteAvailability(id);
    }
}