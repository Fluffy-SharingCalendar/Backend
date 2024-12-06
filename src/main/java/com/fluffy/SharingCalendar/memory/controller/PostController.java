package com.fluffy.SharingCalendar.memory.controller;

import com.fluffy.SharingCalendar.memory.dto.request.ModifyPostRequestDto;
import com.fluffy.SharingCalendar.memory.dto.request.RegisterPostRequestDto;
import com.fluffy.SharingCalendar.memory.dto.response.PagedPostResponse;
import com.fluffy.SharingCalendar.memory.service.PostService;
import com.fluffy.SharingCalendar.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    @PostMapping("/{eventId}")
    public ResponseEntity<Long> register(@PathVariable Integer eventId,
            @RequestHeader(value = "Authorization", required = false) String accessToken,
            @RequestBody RegisterPostRequestDto request) {
        long postIndex = postService.register(eventId, request, jwtUtil.getNickname(accessToken));
        return ResponseEntity.ok(postIndex);
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
