package com.fluffy.SharingCalendar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class PostDetail {
    private Integer postId;
    private Long authorId;
    private String authorNickname;
    private Integer authorProfileImageNo;
    private List<ImageDto> urls;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate eventDate;

    private Integer commentCnt;

    @QueryProjection
    public PostDetail(Integer postId, Long authorId, String authorNickname, Integer authorProfileImageNo, String content, LocalDate eventDate, Integer commentCnt) {
        this.postId = postId;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
        this.authorProfileImageNo = authorProfileImageNo;
        this.content = content;
        this.eventDate = eventDate;
        this.commentCnt = commentCnt;
    }

    public void addUrls(List<ImageDto> urls) {
        this.urls = urls;
    }
}
