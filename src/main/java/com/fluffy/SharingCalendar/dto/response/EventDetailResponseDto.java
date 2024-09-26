package com.fluffy.SharingCalendar.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventDetailResponseDto {
    private String title;
    private String startDate;
    private String endDate;
    private String color;
    private String randomImageUrl;
}
