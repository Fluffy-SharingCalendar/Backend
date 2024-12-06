package com.fluffy.SharingCalendar.calendar.repository;

import static com.fluffy.SharingCalendar.calendar.domain.QCalendar.calendar;
import static com.fluffy.SharingCalendar.calendar.domain.QCalendarMember.calendarMember;

import com.fluffy.SharingCalendar.calendar.dto.response.CalendarSummaryResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CalendarQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public CalendarQuerydslRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<CalendarSummaryResponseDto> findCalendarsByUserId(Integer userId) {
        return queryFactory
                .select(Projections.constructor(
                        CalendarSummaryResponseDto.class,
                        calendar.id,
                        calendar.name,
                        calendar.profileImageUrl,
                        calendarMember.calendar.id.count().intValue()
                ))
                .from(calendarMember)
                .join(calendarMember.calendar, calendar)
                .where(calendarMember.userId.eq(userId))
                .groupBy(calendar.id, calendar.name, calendar.profileImageUrl)
                .fetch();
    }
}

