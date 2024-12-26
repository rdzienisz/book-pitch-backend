package com.wsb.book_pitch.repository;

import com.wsb.book_pitch.model.Booking;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByPitchId(Long pitchId);
}