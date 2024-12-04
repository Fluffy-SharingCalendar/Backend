package com.fluffy.SharingCalendar.calendar.dto.response;

import static com.fluffy.SharingCalendar.common.Constant.DEFAULT_PROFILE_IMAGE_URL;

import com.fluffy.SharingCalendar.calendar.domain.Calendar;
import java.net.URL;
import lombok.Getter;

@Getter
public class CalendarResponseDto {
    private String name;
    private URL profileImageUrl;
    private URL backgroundImage;

    public CalendarResponseDto(Calendar calendar) {
        this.name = calendar.getName();
        this.profileImageUrl = calendar.getProfileImageUrl() != null ? calendar.getProfileImageUrl()
                : DEFAULT_PROFILE_IMAGE_URL;
        this.backgroundImage = calendar.getBackgroundImageUrl();
    }
}
