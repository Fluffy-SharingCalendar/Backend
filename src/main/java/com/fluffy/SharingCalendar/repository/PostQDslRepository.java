package com.fluffy.SharingCalendar.repository;

import com.fluffy.SharingCalendar.dto.ImageDto;
import com.fluffy.SharingCalendar.dto.PostDetailResponseDto;
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

@RequiredArgsConstructor
@Repository
public class PostQDslRepository {

    private final JPAQueryFactory query;

    /*
    user 객체 병합 후 authorId 모두 author 가져오기
    */
    public Page<PostDetailResponseDto> findPostList(Pageable pageable) {
        List<PostDetailResponseDto> postList = query
                .select(Projections.constructor(
                        PostDetailResponseDto.class,
                        post.id,
                        post.authorId,  // 아직 User와 조인하지 않고 userId로 반환
                        post.authorId.stringValue().as("authorNickname"), // 임시로 userId를 nickname에 넣음
                        post.authorId.as("authorProfileImageNo"),         // 임시로 userId를 profileImageNo에 넣음
                        post.content,
                        post.eventDate.stringValue(),
                        comment.count().intValue().as("commentCnt")
                ))
                .from(post)
                .leftJoin(post.comments, comment)
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
