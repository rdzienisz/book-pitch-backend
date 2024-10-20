package com.wsb.book_pitch.repository;

import com.wsb.book_pitch.model.Availability;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN FALSE ELSE TRUE END " +
            "FROM Availability a " +
            "WHERE a.pitch.id = :pitchId " +
            "AND ((:startTime BETWEEN a.startTime AND a.endTime) " +
            "OR (:endTime BETWEEN a.startTime AND a.endTime) " +
            "OR (a.startTime BETWEEN :startTime AND :endTime) " +
            "OR (a.endTime BETWEEN :startTime AND :endTime))")
    boolean isAvailable(@Param("pitchId") Long pitchId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    @Query("SELECT a FROM Availability a WHERE a.pitch.id = :pitchId AND FUNCTION('FORMATDATETIME', a.startTime, 'yyyy-MM-dd') = :date")
    List<Availability> findByPitchIdAndDate(@Param("pitchId") Long pitchId, @Param("date") String date);
}