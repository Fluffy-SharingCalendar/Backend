package com.fluffy.SharingCalendar.calendar.dto.response;

import java.net.URL;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CalendarResponseDto {
    private String name;
    private URL profileImageUrl;
    private String backgroundImage;
}
