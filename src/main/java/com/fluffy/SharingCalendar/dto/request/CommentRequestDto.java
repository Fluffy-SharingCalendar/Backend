package com.fluffy.SharingCalendar.dto.request;

import com.fluffy.SharingCalendar.domain.Comment;
import com.fluffy.SharingCalendar.domain.Post;
import com.fluffy.SharingCalendar.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {

    @Size(min = 1, max = 500, message = "500글자 이하로 작성해 주세요.")
    @NotBlank(message = "내용을 입력해 주세요.")
    private String content;

    public Comment toEntity(Post post, User author) {
        return Comment.builder()
                .post(post)
                .author(author)
                .content(content)
                .build();
    }
}
