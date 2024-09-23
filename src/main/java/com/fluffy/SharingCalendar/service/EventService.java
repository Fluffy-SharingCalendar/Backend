package com.fluffy.SharingCalendar.service;

import com.fluffy.SharingCalendar.domain.Event;
import com.fluffy.SharingCalendar.dto.EventDto;
import com.fluffy.SharingCalendar.repository.CalendarRepository;
import com.fluffy.SharingCalendar.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final CalendarRepository calendarRepository;

    @Transactional
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public List<EventDto> getEventsForCalendar(int calendarId) {
        validateCalendarExists(calendarId);

        return eventRepository.findByCalendarId(calendarId).stream()
                .map(this::toEventDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventDto getEventDetails(int eventId) {
        return eventRepository.findById(eventId)
                .map(this::toEventDto)
                .orElseThrow(() -> new IllegalArgumentException("이벤트를 찾을 수 없습니다."));
    }

    private void validateCalendarExists(int calendarId) {
        if (!calendarRepository.existsById(calendarId)) {
            throw new IllegalArgumentException("캘린더를 찾을 수 없습니다.");
        }
    }

    private EventDto toEventDto(Event event) {
        return EventDto.builder()
                .eventId(event.getEventId())
                .title(event.getTitle())
                .color(event.getColor())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .build();
    }
}
