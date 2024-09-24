package com.fluffy.SharingCalendar.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fluffy.SharingCalendar.domain.PostImage;
import com.fluffy.SharingCalendar.dto.ImageDto;
import com.fluffy.SharingCalendar.repository.PostImageRepository;
import com.fluffy.SharingCalendar.repository.S3Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class S3Service {

    private static final String POST_PATH = "post/";
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");
    private final S3Repository s3Repository;
    private final PostImageRepository postImageRepository;

    @Transactional
    public void delete(int imageId) {
        PostImage postImage = postImageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("이미지가 존재하지 않습니다."));
        s3Repository.deleteFile(postImage.getImageUrl().getPath().substring(1));
        postImageRepository.delete(postImage);
    }

    @Transactional
    public ImageDto upload(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata metadata = createObjectMetadata(file);

            URL responseUrl = s3Repository.uploadFile(metadata, inputStream, POST_PATH + fileName);
            PostImage postImage = savePostImage(responseUrl);

            return toImageDto(postImage);
        } catch (IOException e) {
            throw new IllegalArgumentException("파일 전송에 실패했습니다.", e);
        }
    }

    private ObjectMetadata createObjectMetadata(MultipartFile file) {
        String extension = extractExtension(file.getOriginalFilename());

        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("파일 확장자는 jpg, jpeg, png만 가능합니다.");
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentDisposition("inline");
        metadata.setContentLength(file.getSize());
        metadata.setContentType("image/" + extension);

        return metadata;
    }

    private String createFileName(String originalFileName) {
        return UUID.randomUUID() + "." + extractExtension(originalFileName);
    }

    private String extractExtension(String fileName) {
        return Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1))
                .orElseThrow(() -> new IllegalArgumentException("잘못된 파일 형식입니다."));
    }

    private PostImage savePostImage(URL url) {
        PostImage postImage = PostImage.builder()
                .imageUrl(url)
                .build();
        return postImageRepository.save(postImage);
    }

    private ImageDto toImageDto(PostImage postImage) {
        return ImageDto.builder()
                .imageId(postImage.getId())
                .url(postImage.getImageUrl())
                .build();
    }

}

