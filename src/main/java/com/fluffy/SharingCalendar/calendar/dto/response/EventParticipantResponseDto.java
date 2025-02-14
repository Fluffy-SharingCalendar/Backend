package com.fluffy.SharingCalendar.calendar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventParticipantResponseDto {
    private Integer userId;
    private String name;
    private String profileImageUrl;
}
