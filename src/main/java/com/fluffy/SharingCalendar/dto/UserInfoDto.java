package com.fluffy.SharingCalendar.dto;

import lombok.Builder;


@Builder
public class UserInfoDto {
    private String nickname;
    private String phoneNumber;
    private int profileImageIndex;

    public UserInfoDto(String nickname, String phoneNumber, int profileImageIndex) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.profileImageIndex = profileImageIndex;
    }

}
