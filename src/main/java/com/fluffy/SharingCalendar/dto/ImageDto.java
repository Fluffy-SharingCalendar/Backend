package com.fluffy.SharingCalendar.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.net.URL;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageDto {
    private int imageId;
    private URL url;

    @QueryProjection
    public ImageDto(int imageId, URL url) {
        this.imageId = imageId;
        this.url = url;
    }
}

