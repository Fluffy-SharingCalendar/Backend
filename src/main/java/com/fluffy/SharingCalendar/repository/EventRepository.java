package com.fluffy.SharingCalendar.repository;

import com.fluffy.SharingCalendar.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByCalendarId(int calendarId);
}