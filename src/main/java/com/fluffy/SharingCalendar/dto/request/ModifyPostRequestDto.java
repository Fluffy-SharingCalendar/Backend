package com.fluffy.SharingCalendar.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifyPostRequestDto {
    @Size(min = 1, max = 1000, message = "1000글자 이하로 작성해 주세요.")
    @NotBlank(message = "내용을 입력해 주세요.")
    private String content;

//    private int[] imageIds;
}
