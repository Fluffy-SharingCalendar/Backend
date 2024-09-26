package com.fluffy.SharingCalendar.repository;

import com.fluffy.SharingCalendar.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Integer> {
    @Query("select p from PostImage p where p.postId is null")
    List<PostImage> findAllPostImageNull();
}
