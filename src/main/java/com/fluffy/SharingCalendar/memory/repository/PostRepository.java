package com.fluffy.SharingCalendar.memory.repository;

import com.fluffy.SharingCalendar.memory.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Integer> {
}
