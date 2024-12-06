package com.fluffy.SharingCalendar.memory.repository;

import static com.fluffy.SharingCalendar.memory.domain.QComment.comment;
import static com.fluffy.SharingCalendar.memory.domain.QPost.post;
import static com.fluffy.SharingCalendar.memory.domain.QPostImage.postImage;
import static com.fluffy.SharingCalendar.user.domain.QUser.user;

import com.fluffy.SharingCalendar.memory.domain.Post;
import com.fluffy.SharingCalendar.memory.dto.ImageDto;
import com.fluffy.SharingCalendar.memory.dto.PostDetail;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

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
                        post.author.loginId,
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
                .where(post.eventId.eq(eventId))
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

    public long findPostIndexByPaging(Post createdPost) {
        return query
                .select(post.count())
                .from(post)
                .where(post.eventId.eq(createdPost.getEventId())
                        .and(
                                post.eventDate.gt(createdPost.getEventDate())
                                        .or(
                                                post.eventDate.eq(createdPost.getEventDate())
                                                        .and(post.createdAt.gt(createdPost.getCreatedAt()))
                                        )
                        )
                        .and(post.id.ne(createdPost.getId()))
                )
                .fetchOne();
    }
}
