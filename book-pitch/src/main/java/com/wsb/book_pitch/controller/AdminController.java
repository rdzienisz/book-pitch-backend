package com.wsb.book_pitch.controller;

import com.wsb.book_pitch.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private BookingService bookingService;
}