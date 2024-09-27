package com.fluffy.SharingCalendar.service;

import com.fluffy.SharingCalendar.domain.Comment;
import com.fluffy.SharingCalendar.domain.Post;
import com.fluffy.SharingCalendar.dto.response.CommentResponseDto;
import com.fluffy.SharingCalendar.dto.request.RegisterCommentRequestDto;
import com.fluffy.SharingCalendar.exception.CustomException;
import com.fluffy.SharingCalendar.repository.CommentRepository;
import com.fluffy.SharingCalendar.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.fluffy.SharingCalendar.exception.ErrorCode.COMMENT_NOT_FOUND;
import static com.fluffy.SharingCalendar.exception.ErrorCode.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public void register(RegisterCommentRequestDto request, long authorId) {
        /*
        사용자 검증
         */
        Post post = findPostById(request.getPostId());
        Comment comment = request.toEntity(post, authorId);

        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> readCommentListByPostId(int postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(CommentResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void update(int commentId, String content) {
        /*
        사용자 검증
         */
        Comment comment = findCommentById(commentId);
        comment.update(content);
        commentRepository.save(comment);
    }

    @Transactional
    public void delete(int commentId) {
        /*
        사용자 검증
         */
        Comment comment = findCommentById(commentId);
        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public Post findPostById(int postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Comment findCommentById(int commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));
    }
}
