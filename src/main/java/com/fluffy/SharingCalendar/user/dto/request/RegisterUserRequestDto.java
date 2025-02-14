package com.fluffy.SharingCalendar.user.dto.request;

import com.fluffy.SharingCalendar.user.dto.SecurityAnswerDto;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterUserRequestDto {
    private String name;
    private String loginId;
    private String password;
    private List<SecurityAnswerDto> securityAnswers;
}
