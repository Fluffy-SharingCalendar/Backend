package com.fluffy.SharingCalendar.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SecurityAnswerDto {
    private Integer questionId;
    private String answer;
}
