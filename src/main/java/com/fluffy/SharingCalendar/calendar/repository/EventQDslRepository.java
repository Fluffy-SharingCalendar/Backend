package com.fluffy.SharingCalendar.calendar.repository;

import static com.fluffy.SharingCalendar.memory.domain.QPost.post;
import static com.fluffy.SharingCalendar.memory.domain.QPostImage.postImage;

import com.fluffy.SharingCalendar.calendar.domain.Event;
import com.fluffy.SharingCalendar.calendar.domain.QEvent;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.net.URL;
import java.util.List;

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

    public List<Event> findEventsByCalendarAndMonth(Integer calendarId, int year, int month) {
        QEvent event = QEvent.event;

        return query.selectFrom(event)
                .where(event.calendar.id.eq(calendarId)
                        .and(event.startDate.year().eq(year))
                        .and(event.startDate.month().eq(month)))
                .fetch();
    }
}
