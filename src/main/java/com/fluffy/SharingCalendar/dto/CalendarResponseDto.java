package com.fluffy.SharingCalendar.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CalendarResponseDto {
    private String name;
    private String profileImageUrl;
    private String backgroundImage;
}
