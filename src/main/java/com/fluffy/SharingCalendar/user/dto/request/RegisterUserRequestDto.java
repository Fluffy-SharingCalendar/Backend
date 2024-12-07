package com.fluffy.SharingCalendar.user.dto.request;

import com.fluffy.SharingCalendar.user.dto.SecurityAnswerDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterUserRequestDto {
    private final String name;
    private final String loginId;
    private final String password;
    private final List<SecurityAnswerDto> securityAnswers;
}
