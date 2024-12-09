package com.fluffy.SharingCalendar.calendar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fluffy.SharingCalendar.calendar.domain.Event;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class EventDto {

    private final int eventId;
    private final String title;
    private final String color;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate endDate;

    public EventDto(Event event) {
        eventId = event.getEventId();
        title = event.getTitle();
        color = event.getColor();
        startDate = event.getStartDate();
        endDate = event.getEndDate();
    }
}
