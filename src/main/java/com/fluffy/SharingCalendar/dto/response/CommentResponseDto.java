package com.fluffy.SharingCalendar.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fluffy.SharingCalendar.domain.Comment;
import com.fluffy.SharingCalendar.user.domain.User;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class CommentResponseDto {
    private final int commentId;
    private final long authorId;
    private final String authorName;
    private final String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private final LocalDateTime createdAt;

    public CommentResponseDto(Comment comment) {
        User author = comment.getAuthor();
        this.commentId = comment.getCommentId();
        this.authorId = author.getId();
        this.authorName = author.getName();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
}
