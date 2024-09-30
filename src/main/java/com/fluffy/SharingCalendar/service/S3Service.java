package com.fluffy.SharingCalendar.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fluffy.SharingCalendar.domain.PostImage;
import com.fluffy.SharingCalendar.dto.ImageDto;
import com.fluffy.SharingCalendar.exception.CustomException;
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

    private static final String POST_PATH = "post/";
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");
    private final S3Repository s3Repository;
    private final PostImageRepository postImageRepository;

    @Transactional
    public void delete(int imageId) {
        PostImage postImage = postImageRepository.findById(imageId).orElseThrow(() -> new CustomException(IMAGE_NOT_FOUND));
        s3Repository.deleteFile(postImage.getImageUrl().getPath().substring(1));
        postImageRepository.delete(postImage);
    }

    @Transactional
    public void deleteFiles(List<URL> imageUrls) {
        List<String> keyNames = imageUrls.stream().map(url -> url.getPath().substring(1)).toList();
        s3Repository.deleteFiles(keyNames);
    }

    /*
    이미지 분리 게시글 등록
     */
//    @Transactional
//    public ImageDto upload(MultipartFile file) {
//        try (InputStream inputStream = file.getInputStream()) {
//            String fileName = createFileName(file.getOriginalFilename());
//            ObjectMetadata metadata = createObjectMetadata(file);
//
//            URL responseUrl = s3Repository.uploadFile(metadata, inputStream, POST_PATH + fileName);
//            PostImage postImage = savePostImage(responseUrl);
//
//            return toImageDto(postImage);
//        } catch (IOException e) {
//            throw new CustomException(UNSUCCESSFUL_UPLOAD);
//        }
//    }

    /*
        이미지 동시 게시글 등록
     */
    @Transactional
    public ImageDto upload(MultipartFile file, int postId, int sortOrder) { // sortOrder를 추가로 받음
        try (InputStream inputStream = file.getInputStream()) {
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata metadata = createObjectMetadata(file);

            URL responseUrl = s3Repository.uploadFile(metadata, inputStream, POST_PATH + fileName);
            PostImage postImage = savePostImage(responseUrl, postId, sortOrder);

            return toImageDto(postImage);
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

    /*
    게시글 이미지 분리 등록
     */
//    private PostImage savePostImage(URL url) {
//        PostImage postImage = PostImage.builder()
//                .imageUrl(url)
//                .createdAt(LocalDateTime.now())
//                .build();
//        return postImageRepository.save(postImage);
//    }

    /*
    게시글 이미지 동시 등록
     */
    private PostImage savePostImage(URL url, int postId, int sortOrder) { // sortOrder 값을 포함하여 저장
        PostImage postImage = PostImage.builder().imageUrl(url).createdAt(LocalDateTime.now()).postId(postId).sort(sortOrder) // sortOrder 값을 설정
                .build();
        return postImageRepository.save(postImage);
    }

    private ImageDto toImageDto(PostImage postImage) {
        return ImageDto.builder().imageId(postImage.getId()).url(postImage.getImageUrl()).build();
    }

}

