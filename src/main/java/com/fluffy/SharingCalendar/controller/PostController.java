package com.fluffy.SharingCalendar.controller;

import com.fluffy.SharingCalendar.dto.request.ModifyPostRequestDto;
import com.fluffy.SharingCalendar.dto.request.RegisterPostRequestDto;
import com.fluffy.SharingCalendar.dto.response.PagedPostResponse;
import com.fluffy.SharingCalendar.service.PostService;
import com.fluffy.SharingCalendar.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    @PostMapping("/{eventId}")
    public ResponseEntity<Void> register(@PathVariable Integer eventId,
                                         @RequestHeader(value = "Authorization", required = false) String accessToken,
                                         @RequestBody RegisterPostRequestDto request) {
        postService.register(eventId, request, jwtUtil.getNickname(accessToken));
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

    @PatchMapping("/{postId}")
    public ResponseEntity<Void> modify(@PathVariable Integer postId,
                                       @RequestHeader(value = "Authorization", required = false) String accessToken,
                                       @RequestBody ModifyPostRequestDto request) {
        postService.update(postId, request, jwtUtil.getNickname(accessToken));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable Integer postId,
                                       @RequestHeader(value = "Authorization", required = false) String accessToken) {
        postService.delete(postId, jwtUtil.getNickname(accessToken));
        return ResponseEntity.noContent().build();
    }
}
