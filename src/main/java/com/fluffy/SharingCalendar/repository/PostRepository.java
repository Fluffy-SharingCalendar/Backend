package com.fluffy.SharingCalendar.repository;

import com.fluffy.SharingCalendar.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Integer> {
}
