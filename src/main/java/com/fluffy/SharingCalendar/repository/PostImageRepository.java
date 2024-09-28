package com.fluffy.SharingCalendar.repository;

import com.fluffy.SharingCalendar.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Integer> {
    @Query("select p from PostImage p where p.postId is null")
    List<PostImage> findAllPostImageNull();

    @Modifying(clearAutomatically = true)
    @Query("update PostImage p set p.postId = :postId, p.sort = :sort where p.id = :imageId")
    void updateImageByPostId(@Param("postId") Integer postId, @Param("imageId") Integer imageId, @Param("sort") Integer sort);

    @Modifying(clearAutomatically = true)
    @Query("update PostImage p set p.postId=null where p.postId = :postId")
    void updateAllByPostId(@Param("postId") Integer postId);
}
