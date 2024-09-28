package com.fluffy.SharingCalendar.service;

import com.fluffy.SharingCalendar.domain.Comment;
import com.fluffy.SharingCalendar.domain.Post;
import com.fluffy.SharingCalendar.domain.User;
import com.fluffy.SharingCalendar.dto.request.CommentRequestDto;
import com.fluffy.SharingCalendar.dto.response.CommentResponseDto;
import com.fluffy.SharingCalendar.exception.CustomException;
import com.fluffy.SharingCalendar.repository.CommentRepository;
import com.fluffy.SharingCalendar.repository.PostRepository;
import com.fluffy.SharingCalendar.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.fluffy.SharingCalendar.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Post post;

    @BeforeEach
    void setup() {
        user = User.builder()
                .nickname("yoojeongkwon")
                .phoneNumber("010-1234-5678")
                .profileImageIndex(1)
                .build();

        post = Post.builder()
                .eventId(1)
                .content("test content")
                .eventDate(LocalDate.now())
                .author(user)
                .build();

        userRepository.save(user);
        postRepository.save(post);
    }

    // 댓글 등록 통합 테스트
    @Test
    void registerComment() {
        CommentRequestDto commentRequestDto = new CommentRequestDto("New comment");

        commentService.register(post.getId(), commentRequestDto, "yoojeongkwon");

        List<Comment> comments = commentRepository.findByPostId(post.getId());
        assertEquals(1, comments.size());
        assertEquals("New comment", comments.get(0).getContent());
    }

    // 댓글 등록 시 게시글이 없을 때 예외 처리 통합 테스트
    @Test
    void registerComment_PostNotFound() {
        CommentRequestDto commentRequestDto = new CommentRequestDto("New comment");

        CustomException exception = assertThrows(CustomException.class, () ->
                commentService.register(9999, commentRequestDto, "testNickname"));

        assertEquals(POST_NOT_FOUND, exception.getErrorCode());
    }

    // 댓글 수정 통합 테스트
    @Test
    void updateComment() {
        Comment comment = new Comment(post, user, "test comment");
        commentRepository.save(comment);

        commentService.update(comment.getCommentId(), "Updated comment", "yoojeongkwon");

        Optional<Comment> updatedComment = commentRepository.findById(comment.getCommentId());
        assertTrue(updatedComment.isPresent());
        assertEquals("Updated comment", updatedComment.get().getContent());
    }

    // 댓글 수정 시 작성자가 다를 경우 예외 처리 통합 테스트
    @Test
    void updateComment_NoPermission() {
        Comment comment = new Comment(post, user, "test comment");
        commentRepository.save(comment);

        User anotherUser = User.builder()
                .nickname("anotherUser")
                .phoneNumber("010-5678-1234")
                .profileImageIndex(2)
                .build();
        userRepository.save(anotherUser);

        CustomException exception = assertThrows(CustomException.class, () ->
                commentService.update(comment.getCommentId(), "Updated comment", "anotherUser"));

        assertEquals(NO_PERMISSION_FOR_MODIFICATION, exception.getErrorCode());
    }

    // 댓글 삭제 통합 테스트
    @Test
    void deleteComment() {
        Comment comment = new Comment(post, user, "test comment");
        commentRepository.save(comment);

        commentService.delete(comment.getCommentId(), "yoojeongkwon");

        Optional<Comment> deletedComment = commentRepository.findById(comment.getCommentId());
        assertFalse(deletedComment.isPresent());
    }

    // 댓글 삭제 시 댓글이 없을 때 예외 처리 통합 테스트
    @Test
    void deleteComment_CommentNotFound() {
        CustomException exception = assertThrows(CustomException.class, () ->
                commentService.delete(9999, "yoojeongkwon"));

        assertEquals(COMMENT_NOT_FOUND, exception.getErrorCode());
    }

    // 댓글 목록 조회 통합 테스트
    @Test
    void readCommentListByPostId() {
        Comment comment1 = new Comment(post, user, "comment 1");
        Comment comment2 = new Comment(post, user, "comment 2");
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        List<CommentResponseDto> comments = commentService.readCommentListByPostId(post.getId());

        comments.forEach(c -> System.out.println("-----" + c.toString()));
        assertEquals(2, comments.size());
        assertEquals("comment 1", comments.get(0).getContent());
        assertEquals("comment 2", comments.get(1).getContent());
    }
}
