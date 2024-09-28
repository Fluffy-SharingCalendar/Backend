package com.fluffy.SharingCalendar.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CalendarResponseDto {
    private String name;
    private String profileImageUrl;
    private String backgroundImage;
}
