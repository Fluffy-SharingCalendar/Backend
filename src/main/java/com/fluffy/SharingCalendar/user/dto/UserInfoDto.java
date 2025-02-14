package com.fluffy.SharingCalendar.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoDto {
    private final String name;
    private final String loginId;
    private final boolean notificationStatus;

    public UserInfoDto(String name, String loginId, boolean notificationStatus) {
        this.name = name;
        this.loginId = loginId;
        this.notificationStatus = notificationStatus;
    }
}
