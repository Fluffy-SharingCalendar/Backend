package com.fluffy.SharingCalendar.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EventDto {
    private int eventId;
    private String title;
    private String color;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
