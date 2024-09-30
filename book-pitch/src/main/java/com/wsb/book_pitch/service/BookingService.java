package com.wsb.book_pitch.service;

import com.wsb.book_pitch.model.*;
import com.wsb.book_pitch.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private PitchRepository pitchRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public List<Pitch> getAvailablePitches() {
        return pitchRepository.findAll();
    }

    public Booking createBooking(Long pitchId, String email) {
        Pitch pitch = pitchRepository.findById(pitchId).orElseThrow();
        Booking booking = new Booking();
        booking.setPitch(pitch);
        booking.setUserEmail(email);
        booking.setBookingTime(LocalDateTime.now());
        booking.setIsActive(true);
        return bookingRepository.save(booking);
    }

    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        booking.setIsActive(false);
        bookingRepository.save(booking);
    }

    public Availability createAvailability(Availability availability) {
        return availabilityRepository.save(availability);
    }

    public void deleteAvailability(Long availabilityId) {
        availabilityRepository.deleteById(availabilityId);
    }
}