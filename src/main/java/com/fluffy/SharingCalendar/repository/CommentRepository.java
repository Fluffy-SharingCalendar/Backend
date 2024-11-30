package com.fluffy.SharingCalendar.repository;

import com.fluffy.SharingCalendar.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByPostId(int postId);

    List<Comment> findByPostIdOrderByCreatedAtAsc(int postId);
}
