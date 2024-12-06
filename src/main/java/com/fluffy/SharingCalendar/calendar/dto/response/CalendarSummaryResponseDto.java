package com.fluffy.SharingCalendar.calendar.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class CalendarSummaryResponseDto {

    private final int calendarId;
    private final String name;
    private final String profileImageUrl;
    private final int participantsCount;

    @QueryProjection
    public CalendarSummaryResponseDto(Integer calendarId, String name, String profileImageUrl,
            Integer participantsCount) {
        this.calendarId = calendarId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.participantsCount = participantsCount;
    }
}
