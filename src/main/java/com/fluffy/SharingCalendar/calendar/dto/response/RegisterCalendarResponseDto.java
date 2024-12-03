package com.fluffy.SharingCalendar.calendar.dto.response;


import java.time.LocalDate;
import lombok.Getter;

@Getter
public class RegisterCalendarResponseDto {
    private int calendarId;
    private int currentYear;

    public RegisterCalendarResponseDto(int calendarId) {
        this.calendarId = calendarId;
        this.currentYear = LocalDate.now().getYear();
    }
}
