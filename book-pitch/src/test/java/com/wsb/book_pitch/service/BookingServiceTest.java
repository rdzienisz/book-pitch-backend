package com.wsb.book_pitch.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.wsb.book_pitch.exception.BookingConflictException;
import com.wsb.book_pitch.exception.BookingNotFoundException;
import com.wsb.book_pitch.exception.PitchNotFoundException;
import com.wsb.book_pitch.model.Booking;
import com.wsb.book_pitch.model.Pitch;
import com.wsb.book_pitch.repository.BookingRepository;
import com.wsb.book_pitch.repository.PitchRepository;
import com.wsb.book_pitch.service.BookingService;
import com.wsb.book_pitch.util.TimeSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BookingServiceTest {

    @Mock
    private PitchRepository pitchRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllPitchesWhenGetAvailablePitchesCalled() {
        List<Pitch> pitches = Collections.singletonList(new Pitch());
        when(pitchRepository.findAll()).thenReturn(pitches);

        List<Pitch> result = bookingService.getAvailablePitches();

        assertEquals(pitches, result);
    }

    @Test
    void shouldCreateBookingWhenNoConflictsAndWithinOperatingHours() throws IOException {
        Long pitchId = 1L;
        String email = "test@example.com";
        LocalDateTime startTime = LocalDateTime.of(2023, 10, 10, 9, 0);
        int durationHours = 2;

        Pitch pitch = new Pitch();
        when(pitchRepository.findById(pitchId)).thenReturn(Optional.of(pitch));
        when(bookingRepository.findByPitchId(pitchId)).thenReturn(Collections.emptyList());

        Booking booking = new Booking();
        booking.setPitch(pitch);
        booking.setUserEmail(email);
        booking.setStartTime(startTime);
        booking.setEndTime(startTime.plusHours(durationHours));
        booking.setIsActive(true);
        booking.setId(4123L);

        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.createBooking(pitchId, email, startTime, durationHours);

        assertEquals(booking, result);
        verify(emailService, times(1)).sendEmail(email, "Booking Confirmation",
                "Your booking for pitch 1 is confirmed for 09:00, 2023-10-10.\n"
                        + "If you want to cancel the booking enter this link: http://localhost:8080/api/user/4123/1.");
    }

    @Test
    void shouldThrowBookingConflictExceptionWhenTimeSlotIsUnavailable() {
        Long pitchId = 1L;
        String email = "test@example.com";
        LocalDateTime startTime = LocalDateTime.of(2023, 10, 10, 9, 0);
        int durationHours = 2;

        Pitch pitch = new Pitch();
        when(pitchRepository.findById(pitchId)).thenReturn(Optional.of(pitch));

        Booking existingBooking = new Booking();
        existingBooking.setStartTime(startTime.minusHours(1));
        existingBooking.setEndTime(startTime.plusHours(1));
        existingBooking.setIsActive(true);

        when(bookingRepository.findByPitchId(pitchId)).thenReturn(
                Collections.singletonList(existingBooking));

        assertThrows(BookingConflictException.class, () -> {
            bookingService.createBooking(pitchId, email, startTime, durationHours);
        });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenBookingOutsideOperatingHours() {
        Long pitchId = 1L;
        String email = "test@example.com";
        LocalDateTime startTime = LocalDateTime.of(2023, 10, 10, 7, 0);
        int durationHours = 2;

        Pitch pitch = new Pitch();
        when(pitchRepository.findById(pitchId)).thenReturn(Optional.of(pitch));

        assertThrows(IllegalArgumentException.class, () -> {
            bookingService.createBooking(pitchId, email, startTime, durationHours);
        });
    }

    @Test
    void shouldThrowPitchNotFoundExceptionWhenPitchDoesNotExist() {
        Long pitchId = 1L;
        String email = "test@example.com";
        LocalDateTime startTime = LocalDateTime.of(2023, 10, 10, 9, 0);
        int durationHours = 2;

        when(pitchRepository.findById(pitchId)).thenReturn(Optional.empty());

        assertThrows(PitchNotFoundException.class, () -> {
            bookingService.createBooking(pitchId, email, startTime, durationHours);
        });
    }

    @Test
    void shouldReturnAvailableTimeSlotsWhenCheckAvailabilityCalled() {
        Long pitchId = 1L;
        LocalDate date = LocalDate.of(2023, 10, 10);

        when(bookingRepository.findByPitchId(pitchId)).thenReturn(Collections.emptyList());

        List<TimeSlot> availableSlots = bookingService.checkAvailability(pitchId, date);

        assertFalse(availableSlots.isEmpty());
    }

    @Test
    void shouldCancelBookingWhenBookingExists() {
        Long bookingId = 1L;
        Booking booking = new Booking();
        booking.setIsActive(true);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        bookingService.cancelBooking(bookingId);

        assertFalse(booking.isActive());
        verify(bookingRepository, times(1)).delete(booking);
    }

    @Test
    void shouldThrowBookingNotFoundExceptionWhenBookingDoesNotExist() {
        Long bookingId = 1L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> {
            bookingService.cancelBooking(bookingId);
        });
    }

    @Test
    void shouldReturnBookingsForPitchWhenGetBookingsByPitchCalled() {
        Long pitchId = 1L;
        List<Booking> bookings = Collections.singletonList(new Booking());

        when(bookingRepository.findByPitchId(pitchId)).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByPitch(pitchId);

        assertEquals(bookings, result);
    }

    @Test
    void shouldReturnAllBookingsWhenGetAllBookingsCalled() {
        List<Booking> bookings = Collections.singletonList(new Booking());

        when(bookingRepository.findAll()).thenReturn(bookings);

        List<Booking> result = bookingService.getAllBookings();

        assertEquals(bookings, result);
    }
}