package com.fluffy.SharingCalendar.user.repository;

import static com.fluffy.SharingCalendar.calendar.domain.QCalendarMember.calendarMember;
import static com.fluffy.SharingCalendar.user.domain.QUser.user;

import com.fluffy.SharingCalendar.calendar.dto.response.CalendarMemberResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class UserQuerydslRepository {

    private final JPAQueryFactory query;

    public UserQuerydslRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<CalendarMemberResponseDto> findUsersWithCalendarStatus(int calendarId, String keyword) {
        return query
                .select(Projections.constructor(
                        CalendarMemberResponseDto.class,
                        user,
                        calendarMember.status.coalesce("false") // 상태가 null이면 기본값 설정
                ))
                .from(user)
                .leftJoin(calendarMember)
                .on(
                        calendarMember.userId.eq(user.id)
                                .and(calendarMember.calendar.id.eq(calendarId))
                )
                .where(user.loginId.contains(keyword))
                .fetch();

    }
}
