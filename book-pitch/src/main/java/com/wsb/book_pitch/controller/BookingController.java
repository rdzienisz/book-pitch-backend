package com.wsb.book_pitch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class BookingController {

    @GetMapping("/api/bookings/test")
    public String testEndpoint() {
        return "This is a protected endpoint";
    }
}