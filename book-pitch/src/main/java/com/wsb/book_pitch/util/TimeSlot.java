package com.wsb.book_pitch.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public class TimeSlot {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime end;

    public TimeSlot(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}