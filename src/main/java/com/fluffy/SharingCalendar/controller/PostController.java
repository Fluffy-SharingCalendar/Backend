package com.fluffy.SharingCalendar.controller;

import com.fluffy.SharingCalendar.dto.request.ModifyPostRequestDto;
import com.fluffy.SharingCalendar.dto.request.RegisterPostRequestDto;
import com.fluffy.SharingCalendar.dto.response.PagedPostResponse;
import com.fluffy.SharingCalendar.service.PostService;
import com.fluffy.SharingCalendar.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    /*
    이미지 분리 등록
     */
//    @PostMapping("/{eventId}")
//    public ResponseEntity<Void> register(@PathVariable Integer eventId,
//                                         @RequestHeader(value = "Authorization", required = false) String accessToken,
//                                         @RequestBody RegisterPostRequestDto request) {
//        postService.register(eventId, request, jwtUtil.getNickname(accessToken));
//        return ResponseEntity.noContent().build();
//    }

    /*
    이미지 동시 등록
     */
    @PostMapping(value = "/{eventId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> register(@PathVariable Integer eventId,
                                         @RequestHeader(value = "Authorization", required = false) String accessToken,
                                         @RequestPart(value = "post") RegisterPostRequestDto request,
                                         @RequestPart(value = "file", required = false) MultipartFile[] files) {
        postService.register(eventId, request, files, jwtUtil.getNickname(accessToken));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<PagedPostResponse> readPostList(
            @PathVariable Integer eventId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(postService.readPostList(eventId, pageable));
    }

    /*
    이미지 분리 수정
     */
//    @PatchMapping("/{postId}")
//    public ResponseEntity<Void> modify(@PathVariable Integer postId,
//                                       @RequestHeader(value = "Authorization", required = false) String accessToken,
//                                       @RequestBody ModifyPostRequestDto request) {
//        postService.update(postId, request, jwtUtil.getNickname(accessToken));
//        return ResponseEntity.noContent().build();
//    }

    /*
    이미지 동시 수정
     */
    @PatchMapping(value = "/{postId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> modify(@PathVariable Integer postId,
                                       @RequestHeader(value = "Authorization", required = false) String accessToken,
                                       @RequestPart(value = "post") ModifyPostRequestDto request,
                                       @RequestPart(value = "file", required = false) MultipartFile[] files) {
        postService.update(postId, request, files, jwtUtil.getNickname(accessToken));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable Integer postId,
                                       @RequestHeader(value = "Authorization", required = false) String accessToken) {
        postService.delete(postId, jwtUtil.getNickname(accessToken));
        return ResponseEntity.noContent().build();
    }
}
