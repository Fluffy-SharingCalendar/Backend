package com.fluffy.SharingCalendar.repository;

import com.fluffy.SharingCalendar.dto.ImageDto;
import com.fluffy.SharingCalendar.dto.PostDetail;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.fluffy.SharingCalendar.domain.QComment.comment;
import static com.fluffy.SharingCalendar.domain.QPost.post;
import static com.fluffy.SharingCalendar.domain.QPostImage.postImage;
import static com.fluffy.SharingCalendar.domain.QUser.user;

@RequiredArgsConstructor
@Repository
public class PostQDslRepository {

    private final JPAQueryFactory query;

    public Page<PostDetail> findPostList(int eventId, Pageable pageable) {
        List<PostDetail> postList = query
                .select(Projections.constructor(
                        PostDetail.class,
                        post.id,
                        post.author.id,
                        post.author.nickname.as("authorNickname"),
                        post.author.profileImageIndex.as("authorProfileImageNo"),
                        post.content,
                        post.eventDate,
                        comment.count().intValue().as("commentCnt")
                ))
                .from(post)
                .leftJoin(post.comments, comment)
                .leftJoin(post.author, user)
                .where(post.eventId.eq(eventId))
                .groupBy(post.id)
                .orderBy(post.eventDate.desc(), post.createdAt.desc())  // eventDate 기준 1차 정렬, createdAt 기준 2차 정렬
                .offset(pageable.getOffset())  // 시작점 (페이지 시작 위치)
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetch();

        postList.forEach(postDto -> {
            List<ImageDto> imageDtos = fetchImageDtos(postDto.getPostId()); // postId를 통해 이미지 리스트 조회
            postDto.addUrls(imageDtos);
        });
        long total = query
                .select(post.count())
                .from(post)
                .fetchOne();

        return new PageImpl<>(postList, pageable, total);
    }

    private List<ImageDto> fetchImageDtos(int postId) {
        return query
                .select(Projections.constructor(
                        ImageDto.class,
                        postImage.id,
                        postImage.imageUrl
                ))
                .from(postImage)
                .where(postImage.postId.eq(postId))
                .orderBy(postImage.sort.asc())
                .fetch();
    }

}
