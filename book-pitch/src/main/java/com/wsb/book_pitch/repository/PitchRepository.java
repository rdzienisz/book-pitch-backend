package com.wsb.book_pitch.repository;

import com.wsb.book_pitch.model.Pitch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PitchRepository extends JpaRepository<Pitch, Long> {
    // Additional query methods can be defined here if needed
}
