package com.wsb.book_pitch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailabilityRepository extends JpaRepository<Object, Long> {
    // Additional query methods can be defined here if needed
}
