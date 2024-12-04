package com.fluffy.SharingCalendar.common.image;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fluffy.SharingCalendar.memory.domain.PostImage;
import com.fluffy.SharingCalendar.exception.CustomException;
import com.fluffy.SharingCalendar.memory.dto.ImageDto;
import com.fluffy.SharingCalendar.memory.repository.PostImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.fluffy.SharingCalendar.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class S3Service {

    private static final String PROFILE_PATH = "profile/";
    private static final String POST_PATH = "post/";
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");
    private final S3Repository s3Repository;
    private final PostImageRepository postImageRepository;

    @Transactional
    public void deletePostImage(int imageId) {
        PostImage postImage = postImageRepository.findById(imageId)
                .orElseThrow(() -> new CustomException(IMAGE_NOT_FOUND));
        deleteImage(postImage.getImageUrl());
        postImageRepository.delete(postImage);
    }

    @Transactional
    public void deleteImage(URL imageUrl) {
        s3Repository.deleteFile(imageUrl.getPath().substring(1));
    }

    @Transactional
    public void deleteFiles(List<URL> imageUrls) {
        List<String> keyNames = imageUrls.stream().map(url -> url.getPath().substring(1)).toList();
        s3Repository.deleteFiles(keyNames);
    }

    @Transactional
    public ImageDto uploadPostImage(MultipartFile file) {
        URL responseUrl = uploadFileToS3(file, POST_PATH);
        PostImage postImage = savePostImage(responseUrl);
        return toImageDto(postImage);
    }

    @Transactional
    public URL uploadProfileImage(MultipartFile file) {
        return uploadFileToS3(file, PROFILE_PATH);
    }

    private URL uploadFileToS3(MultipartFile file, String path) {
        try (InputStream inputStream = file.getInputStream()) {
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata metadata = createObjectMetadata(file);
            return s3Repository.uploadFile(metadata, inputStream, path + fileName);
        } catch (IOException e) {
            throw new CustomException(UNSUCCESSFUL_UPLOAD);
        }
    }

    private ObjectMetadata createObjectMetadata(MultipartFile file) {
        String extension = extractExtension(file.getOriginalFilename());

        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new CustomException(INVALID_EXTENSION);
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
        return Optional.ofNullable(fileName).filter(f -> f.contains(".")).map(f -> f.substring(fileName.lastIndexOf(".") + 1)).orElseThrow(() -> new CustomException(INVALID_EXTENSION));
    }

    private PostImage savePostImage(URL url) {
        PostImage postImage = PostImage.builder()
                .imageUrl(url)
                .createdAt(LocalDateTime.now())
                .build();
        return postImageRepository.save(postImage);
    }

    private ImageDto toImageDto(PostImage postImage) {
        return ImageDto.builder().imageId(postImage.getId()).url(postImage.getImageUrl()).build();
    }
}

