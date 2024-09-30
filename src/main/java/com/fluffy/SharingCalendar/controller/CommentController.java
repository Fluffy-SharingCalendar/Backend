package com.fluffy.SharingCalendar.controller;

import com.fluffy.SharingCalendar.dto.request.CommentRequestDto;
import com.fluffy.SharingCalendar.dto.response.CommentResponseDto;
import com.fluffy.SharingCalendar.service.CommentService;
import com.fluffy.SharingCalendar.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/comments")
public class CommentController {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @PostMapping("/{postId}")
    public ResponseEntity<Void> register(@PathVariable Integer postId,
                                         @RequestHeader(value = "Authorization", required = false) String accessToken,
                                         @Validated @RequestBody CommentRequestDto request) {
        commentService.register(postId, request, jwtUtil.getNickname(accessToken));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponseDto>> readCommentList(
            @PathVariable Integer postId) {
        return ResponseEntity.ok(commentService.readCommentListByPostId(postId));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> modify(@PathVariable Integer commentId,
                                       @RequestHeader(value = "Authorization", required = false) String accessToken,
                                       @Validated @RequestBody CommentRequestDto request) {
        commentService.update(commentId, request.getContent(), jwtUtil.getNickname(accessToken));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Integer commentId,
                                       @RequestHeader(value = "Authorization", required = false) String accessToken) {
        commentService.delete(commentId, jwtUtil.getNickname(accessToken));
        return ResponseEntity.noContent().build();
    }
}
