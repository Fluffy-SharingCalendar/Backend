package com.fluffy.SharingCalendar.dto;

import com.fluffy.SharingCalendar.domain.Post;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRequest {
    private int eventId;
    private String eventDate;
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
