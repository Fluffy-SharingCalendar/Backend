package com.fluffy.SharingCalendar.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class PostDetailResponseDto {
    private int postId;
    private int authorId;
    private String authorNickname;
    private int authorProfileImageNo;
    private List<ImageDto> urls;
    private String content;
    private String eventDate;
    private int commentCnt;

    @QueryProjection
    public PostDetailResponseDto(Integer postId, Integer authorId, String authorNickname, Integer authorProfileImageNo, String content, String eventDate, Integer commentCnt) {
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
