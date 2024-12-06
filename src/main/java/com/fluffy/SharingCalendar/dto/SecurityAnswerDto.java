package com.fluffy.SharingCalendar.dto;

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
