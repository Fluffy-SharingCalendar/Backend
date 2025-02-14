package com.fluffy.SharingCalendar.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerifyUserIdentityRequestDto {
    private String loginId;
    private Integer questionId;
    private String answer;
}
