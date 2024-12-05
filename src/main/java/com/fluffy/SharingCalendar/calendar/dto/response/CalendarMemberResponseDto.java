package com.fluffy.SharingCalendar.calendar.dto.response;

import com.fluffy.SharingCalendar.calendar.domain.CalendarMember;
import lombok.Getter;

@Getter
public class CalendarMemberResponseDto {
    private Integer userId;
    private String name;
    private String profileImageUrl;
    private String status;

    public CalendarMemberResponseDto(CalendarMember calendarMember) {
        this.userId = calendarMember.getUserId();
        this.name = calendarMember.getProfileName();
        this.profileImageUrl = calendarMember.getProfileName();
        this.status = calendarMember.getStatus();
    }
}
