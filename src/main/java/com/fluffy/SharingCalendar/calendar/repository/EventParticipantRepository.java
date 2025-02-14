package com.fluffy.SharingCalendar.calendar.repository;

import com.fluffy.SharingCalendar.calendar.domain.EventParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, Integer> {

}
