package com.fluffy.SharingCalendar.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fluffy.SharingCalendar.dto.EventDto;
import lombok.Getter;

import java.net.URL;
import java.time.LocalDate;

@Getter
public class EventDetailResponseDto {
    private final String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private final LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private final LocalDate endDate;

    private final String color;
    private final URL randomImageUrl;

    public EventDetailResponseDto(EventDto eventDto, URL randomImageUrl) {
        this.title = eventDto.getTitle();
        this.startDate = eventDto.getStartDate();
        this.endDate = eventDto.getEndDate();
        this.color = eventDto.getColor();
        this.randomImageUrl = randomImageUrl;
    }
}
