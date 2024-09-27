package com.fluffy.SharingCalendar.repository;

import com.fluffy.SharingCalendar.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
}
