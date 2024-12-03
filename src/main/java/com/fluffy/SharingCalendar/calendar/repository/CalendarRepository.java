package com.fluffy.SharingCalendar.calendar.repository;

import com.fluffy.SharingCalendar.calendar.domain.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Integer> {
}