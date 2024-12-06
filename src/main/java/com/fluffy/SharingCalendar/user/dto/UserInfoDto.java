package com.fluffy.SharingCalendar.user.dto;

import lombok.Builder;


@Builder
public class UserInfoDto {
    private String name;
    private String loginId;
    private boolean notificationStatus;

    public UserInfoDto(String name, String loginId, boolean notificationStatus) {
        this.name = name;
        this.loginId = loginId;
        this.notificationStatus = notificationStatus;
    }

}
