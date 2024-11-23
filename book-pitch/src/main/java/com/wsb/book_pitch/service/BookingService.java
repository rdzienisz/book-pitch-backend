package com.wsb.book_pitch.service;

import com.wsb.book_pitch.exception.BookingConflictException;
import com.wsb.book_pitch.model.Booking;
import com.wsb.book_pitch.model.Pitch;
import com.wsb.book_pitch.repository.BookingRepository;
import com.wsb.book_pitch.repository.PitchRepository;
import com.wsb.book_pitch.util.TimeSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
@Service
public class BookingService {

    @Autowired
    private PitchRepository pitchRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private static final LocalTime OPENING_TIME = LocalTime.of(8, 0);
    private static final LocalTime CLOSING_TIME = LocalTime.of(22, 0);

    public List<Pitch> getAvailablePitches() {
        return pitchRepository.findAll();
    }

    public Booking createBooking(Long pitchId, String email, LocalDateTime startTime, int durationHours) {
        Pitch pitch = pitchRepository.findById(pitchId).orElseThrow();
        LocalDateTime endTime = startTime.plusHours(durationHours);

        if (!isWithinOperatingHours(startTime.toLocalTime(), endTime.toLocalTime())) {
            throw new IllegalArgumentException("Booking times must be between 8 AM and 10 PM.");
        }

        if (!isPitchAvailable(pitchId, startTime, endTime)) {
            throw new BookingConflictException("The selected time slot is already booked. Please choose a different time.");
        }

        Booking booking = new Booking();
        booking.setPitch(pitch);
        booking.setUserEmail(email);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setIsActive(true);
        return bookingRepository.save(booking);
    }

    private boolean isWithinOperatingHours(LocalTime startTime, LocalTime endTime) {
        return !startTime.isBefore(OPENING_TIME) && !endTime.isAfter(CLOSING_TIME);
    }

    private boolean isPitchAvailable(Long pitchId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> bookings = bookingRepository.findByPitchId(pitchId);
        for (Booking booking : bookings) {
            if (booking.isActive() && booking.getEndTime().isAfter(startTime) && booking.getStartTime().isBefore(endTime)) {
                return false;
            }
        }
        return true;
    }

    public List<TimeSlot> checkAvailability(Long pitchId, LocalDate date) {
        List<TimeSlot> availableSlots = new ArrayList<>();

        for (LocalTime time = OPENING_TIME; time.isBefore(CLOSING_TIME); time = time.plusHours(1)) {
            LocalDateTime slotStart = LocalDateTime.of(date, time);
            LocalDateTime slotEnd = slotStart.plusHours(1);
            if (isPitchAvailable(pitchId, slotStart, slotEnd)) {
                availableSlots.add(new TimeSlot(time, time.plusHours(1)));
            }
        }
        return availableSlots;
    }

    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        booking.setIsActive(false);
        bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByPitch(Long pitchId) {
        return bookingRepository.findByPitchId(pitchId);
    }
}