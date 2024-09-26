package com.fluffy.SharingCalendar.dto;

import lombok.Builder;
import lombok.Getter;

import java.net.URL;

@Getter
@Builder
public class ImageDto {
    private int imageId;
    private URL url;
}
