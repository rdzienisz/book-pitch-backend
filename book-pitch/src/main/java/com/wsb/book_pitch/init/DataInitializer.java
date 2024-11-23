package com.wsb.book_pitch.init;

import com.wsb.book_pitch.model.Pitch;
import com.wsb.book_pitch.repository.PitchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PitchRepository pitchRepository;

    @Override
    public void run(String... args) {
        // Create pitches if they don't exist
        if (pitchRepository.count() == 0) {
            List<Pitch> pitches = List.of(
                    new Pitch(null, "Pitch 1", "Standard pitch"),
                    new Pitch(null, "Pitch 2", "Standard pitch"),
                    new Pitch(null, "Pitch 3", "Standard pitch"),
                    new Pitch(null, "Pitch 4", "Standard pitch")
            );
            pitchRepository.saveAll(pitches);
        }
    }
}