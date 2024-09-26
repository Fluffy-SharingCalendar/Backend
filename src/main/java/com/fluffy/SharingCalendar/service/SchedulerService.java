package com.fluffy.SharingCalendar.service;

import com.fluffy.SharingCalendar.domain.PostImage;
import com.fluffy.SharingCalendar.repository.PostImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Log4j2
@RequiredArgsConstructor
public class SchedulerService {

    private static final int PARTITION_SIZE = 1000;

    private final S3Service s3Service;
    private final PostImageRepository postImageRepository;

    @Scheduled(cron = "* * 3 * * *", zone = "Asia/Seoul")
    @Transactional
    public void deleteImages() {
        log.info("========== 이미지 삭제 스케줄러 실행 ==========");

        List<PostImage> imagesToDelete = getImagesToDelete();
        partitionAndDelete(imagesToDelete);
    }

    private List<PostImage> getImagesToDelete() {
        return postImageRepository.findAllPostImageNull().stream()
                .filter(this::isOlderThan24Hours)
                .toList();
    }

    private boolean isOlderThan24Hours(PostImage image) {
        return Duration.between(image.getCreatedAt(), LocalDateTime.now()).toHours() >= 24;
    }

    private void partitionAndDelete(List<PostImage> images) {
        List<URL> urls = getUrls(images);

        IntStream.range(0, (urls.size() + PARTITION_SIZE - 1) / PARTITION_SIZE)
                .mapToObj(i -> urls.subList(i * PARTITION_SIZE, Math.min((i + 1) * PARTITION_SIZE, urls.size())))
                .forEach(s3Service::deleteFiles);

        postImageRepository.deleteAll(images);
        log.info("삭제된 이미지의 수: " + images.size());
    }

    private List<URL> getUrls(List<PostImage> images) {
        return images.stream()
                .map(PostImage::getImageUrl)
                .collect(Collectors.toList());
    }
}
