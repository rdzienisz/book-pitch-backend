package com.wsb.book_pitch.repository;

import com.wsb.book_pitch.model.Booking;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByPitchId(Long pitchId);

    @Query("SELECT b FROM Booking b WHERE b.pitch.id = :pitchId AND FUNCTION('FORMATDATETIME', b.startTime, 'yyyy-MM-dd') = :date")
    List<Booking> findByPitchIdAndDate(@Param("pitchId") Long pitchId, @Param("date") String date);
}