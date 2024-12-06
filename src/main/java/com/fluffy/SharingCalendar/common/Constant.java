package com.fluffy.SharingCalendar.common;

import static com.fluffy.SharingCalendar.exception.ErrorCode.SERVER_ERROR;

import com.fluffy.SharingCalendar.exception.CustomException;
import java.net.MalformedURLException;
import java.net.URL;

public class Constant {

    public static final URL DEFAULT_PROFILE_IMAGE_URL;

    /*
    추후 기본 이미지 url이 결정되면 수정 필요
     */
    static {
        try {
            DEFAULT_PROFILE_IMAGE_URL = new URL("https://temp-url");
        } catch (MalformedURLException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    private Constant() {
    }
}
