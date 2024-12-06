package com.fluffy.SharingCalendar.memory.service;

import com.fluffy.SharingCalendar.memory.domain.Comment;
import com.fluffy.SharingCalendar.memory.domain.Post;
import com.fluffy.SharingCalendar.user.domain.User;
import com.fluffy.SharingCalendar.memory.dto.request.CommentRequestDto;
import com.fluffy.SharingCalendar.memory.dto.response.CommentResponseDto;
import com.fluffy.SharingCalendar.exception.CustomException;
import com.fluffy.SharingCalendar.memory.repository.CommentRepository;
import com.fluffy.SharingCalendar.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.fluffy.SharingCalendar.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    @Transactional
    public void register(int postId, CommentRequestDto request, String nickname) {
        Post post = postService.findByPostId(postId);
        User user = userService.findByLoginId(nickname);

        Comment comment = request.toEntity(post, user);

        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> readCommentListByPostId(int postId) {
        postService.findByPostId(postId);
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
        return comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void update(int commentId, String content, String nickname) {
        Comment comment = findCommentById(commentId);
        User user = userService.findByLoginId(nickname);

        validateAccess(comment.getAuthor().getId(), user.getId());

        comment.update(content);
        commentRepository.save(comment);
    }

    @Transactional
    public void delete(int commentId, String nickname) {
        Comment comment = findCommentById(commentId);
        User user = userService.findByLoginId(nickname);

        validateAccess(comment.getAuthor().getId(), user.getId());

        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public Comment findCommentById(int commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));
    }

    private void validateAccess(long registerId, long userId) {
        if (registerId != userId) {
            throw new CustomException(NO_PERMISSION_FOR_MODIFICATION);
        }
    }
}
