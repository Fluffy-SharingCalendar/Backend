package com.fluffy.SharingCalendar.dto.request;

import com.fluffy.SharingCalendar.domain.Post;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterPostRequestDto {

    @Min(value = 0)
    private int eventId;

    @Pattern(regexp = "\\d{4}.\\d{2}.\\d{2}", message = "날짜는 yyyy.MM.dd 형식이어야 합니다.")
    private String eventDate;

    @Size(min=1, max=1000, message = "1000글자 이하로 작성해 주세요.")
    @NotBlank(message = "내용을 입력해 주세요.")
    private String content;

    private int[] imageIds;

    public Post toEntity(int userId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDate parsedEventDate = LocalDate.parse(eventDate, formatter);

        return Post.builder()
                .eventId(eventId)
                .authorId(userId)
                .content(content)
                .eventDate(parsedEventDate)
                .build();
    }
}
