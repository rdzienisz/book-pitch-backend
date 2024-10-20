package com.wsb.book_pitch.service;

import com.wsb.book_pitch.model.*;
import com.wsb.book_pitch.repository.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.stream.Collectors;
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

    public Booking createBooking(Long pitchId, String email, LocalDateTime startTime, int durationHours) {
        Pitch pitch = pitchRepository.findById(pitchId).orElseThrow();
        // Calculate the end time based on the booking duration
        LocalDateTime endTime = startTime.plusHours(durationHours);
        // Check if the pitch is available for the requested time slot
        boolean isAvailable = availabilityRepository.isAvailable(pitchId, startTime, endTime);
        if (!isAvailable) {
            throw new IllegalStateException("Pitch is not available for the selected time slot.");
        }
        Booking booking = new Booking();
        booking.setPitch(pitch);
        booking.setUserEmail(email);
        booking.setStartTime(startTime); // Set the start time
        booking.setEndTime(endTime); // Set the end time
        booking.setIsActive(true);
        return bookingRepository.save(booking);
    }

    public List<LocalTime> checkAvailability(Long pitchId, LocalDate date) {
        String dateString = date.toString();  // Convert LocalDate to String for query
        // Retrieve all availabilities for the pitch on the specified date
        List<Availability> availabilities = availabilityRepository.findByPitchIdAndDate(pitchId, dateString);
        // Retrieve all bookings for the pitch on the specified date
        List<Booking> bookings = bookingRepository.findByPitchIdAndDate(pitchId, dateString);
        // Prepare a list of all available slots initially
        List<LocalTime> availableSlots = new ArrayList<>();
        for (Availability availability : availabilities) {
            LocalTime startTime = availability.getStartTime().toLocalTime();
            LocalTime endTime = availability.getEndTime().toLocalTime();
            while (startTime.isBefore(endTime)) {
                availableSlots.add(startTime);
                startTime = startTime.plusHours(1); // Assuming 1-hour slots
            }
        }
        // Remove slots that overlap with bookings
        for (Booking booking : bookings) {
            LocalTime bookingStart = booking.getStartTime().toLocalTime();
            LocalTime bookingEnd = booking.getEndTime().toLocalTime();
            availableSlots = availableSlots.stream()
                    .filter(slot -> slot.isBefore(bookingStart) || slot.isAfter(bookingEnd.minusHours(1)))
                    .collect(Collectors.toList());
        }
        return availableSlots;
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

    public List<Booking> getBookingsByPitch(Long pitchId) {
        return bookingRepository.findByPitchId(pitchId);
    }
}