package com.fluffy.SharingCalendar.calendar.repository;

import com.fluffy.SharingCalendar.calendar.domain.CalendarMember;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarMemberRepository extends JpaRepository<CalendarMember, Integer> {

    Optional<CalendarMember> findByCalendarIdAndUserId(Integer calendarId, Integer userId);
}
