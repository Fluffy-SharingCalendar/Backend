package com.fluffy.SharingCalendar.controller;

import com.fluffy.SharingCalendar.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {

    private final S3Service s3Service;

    /*
    게시글 이미지 동시 등록
     */
//    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ResponseEntity<ImageDto> uploadImage(@RequestPart(name = "file") MultipartFile file) {
//        return ResponseEntity.ok(s3Service.upload(file));
//    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable(name = "imageId") int imageId) {
        s3Service.delete(imageId);
        return ResponseEntity.ok().build();
    }
}
