package com.fluffy.SharingCalendar.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.net.URL;
import java.util.List;

import static com.fluffy.SharingCalendar.domain.QPost.post;
import static com.fluffy.SharingCalendar.domain.QPostImage.postImage;

@Repository
public class EventQDslRepository {

    private final JPAQueryFactory query;

    public EventQDslRepository(JPAQueryFactory query) {
        this.query = query;
    }

    public List<URL> findImagesByEventId(int eventId) {
        return query
                .select(postImage.imageUrl)
                .from(postImage)
                .join(post).on(postImage.postId.eq(post.id)) // postImage와 post를 조인
                .where(post.eventId.eq(eventId)) // eventId로 필터링
                .fetch();
    }
}
