package com.fluffy.SharingCalendar.calendar.repository;

import com.fluffy.SharingCalendar.calendar.domain.CalendarMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarMemberRepository extends JpaRepository<CalendarMember, Integer> {
}
