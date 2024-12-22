package com.wsb.book_pitch.controller;

import com.wsb.book_pitch.model.Booking;
import com.wsb.book_pitch.service.BookingService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private BookingService bookingService;

    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> getAllBookings() {
        logger.info("Received request for all bookings");
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/booking/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        try {
            logger.info("Received request to delete booking with id: {}", id);
            bookingService.cancelBooking(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting booking with id: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }
}