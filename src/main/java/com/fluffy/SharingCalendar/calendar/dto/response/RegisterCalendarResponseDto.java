package com.fluffy.SharingCalendar.calendar.dto.response;


import java.time.LocalDate;
import lombok.Getter;

@Getter
public class RegisterCalendarResponseDto {
    private final int calendarId;
    private final int currentYear;

    public RegisterCalendarResponseDto(int calendarId) {
        this.calendarId = calendarId;
        this.currentYear = LocalDate.now().getYear();
    }
}
