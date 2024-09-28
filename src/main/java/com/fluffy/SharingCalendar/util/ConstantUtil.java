package com.fluffy.SharingCalendar.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ConstantUtil {

    public static final List<URL> DEFAULT_IMAGE_URLS;

    static {
        try {
            DEFAULT_IMAGE_URLS = Arrays.asList(
                    new URL("https://fluffy-image-bucket.s3.ap-northeast-2.amazonaws.com/base/base1.jpg"),
                    new URL("https://fluffy-image-bucket.s3.ap-northeast-2.amazonaws.com/base/base2.jpg"),
                    new URL("https://fluffy-image-bucket.s3.ap-northeast-2.amazonaws.com/base/base3.jpg"),
                    new URL("https://fluffy-image-bucket.s3.ap-northeast-2.amazonaws.com/base/base4.jpg"),
                    new URL("https://fluffy-image-bucket.s3.ap-northeast-2.amazonaws.com/base/base5.jpg")
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL format", e);  // URL이 잘못되었을 경우 예외 처리
        }
    }

    public static URL getRandomDefaultImageUrl() {
        Random random = new Random();
        List<URL> defaultImages = ConstantUtil.DEFAULT_IMAGE_URLS;
        return defaultImages.get(random.nextInt(defaultImages.size()));
    }
}
