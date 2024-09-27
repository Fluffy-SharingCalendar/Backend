package com.fluffy.SharingCalendar.dto;

import lombok.Builder;
import lombok.Data;


@Builder
public class UserInfoDTO {
    private String nickname;
    private String phoneNumber;
    private int profileImageIndex;

    public UserInfoDTO(String nickname, String phoneNumber, int profileImageIndex) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.profileImageIndex = profileImageIndex;
    }

}
