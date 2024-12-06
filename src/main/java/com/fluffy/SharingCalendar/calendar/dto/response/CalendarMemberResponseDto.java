package com.fluffy.SharingCalendar.calendar.dto.response;

import static com.fluffy.SharingCalendar.common.Constant.DEFAULT_PROFILE_IMAGE_URL;

import com.fluffy.SharingCalendar.calendar.domain.CalendarMember;
import com.fluffy.SharingCalendar.user.domain.User;
import com.querydsl.core.annotations.QueryProjection;
import java.net.URL;
import lombok.Getter;

@Getter
public class CalendarMemberResponseDto {
    private Integer userId;
    private String name;
    private URL profileImageUrl;
    private String status;  //false, invited, accepted

    public CalendarMemberResponseDto(CalendarMember calendarMember) {
        this.userId = calendarMember.getUserId();
        this.name = calendarMember.getProfileName();
        this.profileImageUrl = calendarMember.getProfileImageUrl();
        this.status = calendarMember.getStatus();
    }

    @QueryProjection
    public CalendarMemberResponseDto(User user, String status) {
        this.userId = user.getId();
        this.name = user.getName();
        this.profileImageUrl = DEFAULT_PROFILE_IMAGE_URL;
        this.status = status;
    }
}
