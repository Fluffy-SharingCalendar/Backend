package com.fluffy.SharingCalendar.dto.request;

import com.fluffy.SharingCalendar.dto.SecurityAnswerDto;
import java.util.List;
import lombok.Getter;

@Getter
public class RegisterUserRequestDto {
    private String name;
    private String loginId;
    private String password;
    private List<SecurityAnswerDto> securityAnswers;
}
