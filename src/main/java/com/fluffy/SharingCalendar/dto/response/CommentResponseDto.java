package com.fluffy.SharingCalendar.dto.response;

import com.fluffy.SharingCalendar.domain.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private int commentId;
    private int authorId;
    private String authorNickname;
    private int authorProfileNo;
    private String content;
    private String createAt;

    public static CommentResponseDto from(Comment comment) {
        /*
        user 병합후 from
         */
        return new CommentResponseDto();
    }
}
