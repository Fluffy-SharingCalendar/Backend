package com.fluffy.SharingCalendar.service;

import com.fluffy.SharingCalendar.domain.Comment;
import com.fluffy.SharingCalendar.domain.Post;
import com.fluffy.SharingCalendar.domain.User;
import com.fluffy.SharingCalendar.dto.request.CommentRequestDto;
import com.fluffy.SharingCalendar.dto.response.CommentResponseDto;
import com.fluffy.SharingCalendar.exception.CustomException;
import com.fluffy.SharingCalendar.repository.CommentRepository;
import com.fluffy.SharingCalendar.repository.PostRepository;
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
        User user = userService.findByNickname(nickname);

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
        User user = userService.findByNickname(nickname);

        validateAccess(comment.getAuthor().getId(), user.getId());

        comment.update(content);
        commentRepository.save(comment);
    }

    @Transactional
    public void delete(int commentId, String nickname) {
        Comment comment = findCommentById(commentId);
        User user = userService.findByNickname(nickname);

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
