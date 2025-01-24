package com.fluffy.SharingCalendar.user.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequestDto {
    private final String loginId;
    private final String password;
}
