package com.fluffy.SharingCalendar.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fluffy.SharingCalendar.domain.Post;
import com.fluffy.SharingCalendar.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterPostRequestDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate eventDate;

    @Size(min = 1, max = 1000, message = "1000글자 이하로 작성해 주세요.")
    @NotBlank(message = "내용을 입력해 주세요.")
    private String content;

    public Post toEntity(User user, int eventId) {
        return Post.builder()
                .eventId(eventId)
                .author(user)
                .content(content)
                .eventDate(eventDate)
                .build();
    }
}
