package com.wsb.book_pitch.init;

import com.wsb.book_pitch.model.Availability;
import com.wsb.book_pitch.model.Pitch;
import com.wsb.book_pitch.repository.AvailabilityRepository;
import com.wsb.book_pitch.repository.PitchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PitchRepository pitchRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Override
    public void run(String... args) {
        // Create pitches if they don't exist
        if (pitchRepository.count() == 0) {
            List<Pitch> pitches = List.of(
                    new Pitch(null, "Pitch 1", "Standard pitch", true),
                    new Pitch(null, "Pitch 2", "Standard pitch", true),
                    new Pitch(null, "Pitch 3", "Standard pitch", true),
                    new Pitch(null, "Pitch 4", "Standard pitch", true)
            );
            pitchRepository.saveAll(pitches);
        }

        // Create availability slots
        List<Pitch> pitches = pitchRepository.findAll();
        List<Availability> availabilities = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate endOfNextWeek = today.plusWeeks(1).with(java.time.DayOfWeek.SUNDAY);

        for (Pitch pitch : pitches) {
            LocalDate currentDate = today;
            while (!currentDate.isAfter(endOfNextWeek)) {
                LocalDateTime startTime = LocalDateTime.of(currentDate, LocalTime.of(8, 0));
                LocalDateTime endTime = LocalDateTime.of(currentDate, LocalTime.of(20, 0));

                Availability availability = new Availability();
                availability.setPitch(pitch);
                availability.setStartTime(startTime);
                availability.setEndTime(endTime);
                availabilities.add(availability);

                currentDate = currentDate.plusDays(1);
            }
        }

        availabilityRepository.saveAll(availabilities);
    }
}