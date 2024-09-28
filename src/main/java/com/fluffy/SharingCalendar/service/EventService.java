package com.fluffy.SharingCalendar.service;

import com.fluffy.SharingCalendar.domain.Event;
import com.fluffy.SharingCalendar.dto.EventDto;
import com.fluffy.SharingCalendar.dto.response.EventDetailResponseDto;
import com.fluffy.SharingCalendar.exception.CustomException;
import com.fluffy.SharingCalendar.exception.ErrorCode;
import com.fluffy.SharingCalendar.repository.CalendarRepository;
import com.fluffy.SharingCalendar.repository.EventQDslRepository;
import com.fluffy.SharingCalendar.repository.EventRepository;
import com.fluffy.SharingCalendar.util.ConstantUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.fluffy.SharingCalendar.exception.ErrorCode.CALENDAR_NOT_FOUND;
import static com.fluffy.SharingCalendar.exception.ErrorCode.EVENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class EventService {

    public static final URL DEFAULT_PROFILE_IMAGE_URL = ConstantUtil.getRandomDefaultImageUrl();
    private final EventRepository eventRepository;
    private final EventQDslRepository eventQDslRepository;
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
    public EventDetailResponseDto getEventDetails(int eventId) {
        EventDto eventDto = eventRepository.findById(eventId)
                .map(this::toEventDto)
                .orElseThrow(() -> new CustomException(EVENT_NOT_FOUND));

        URL url = getRandomImageForEvent(eventId);

        return new EventDetailResponseDto(eventDto, url);
    }

    public URL getRandomImageForEvent(int eventId) {
        List<URL> images = eventQDslRepository.findImagesByEventId(eventId);

        if (images.isEmpty()) {
            return DEFAULT_PROFILE_IMAGE_URL;
        }

        Random random = new Random();
        return images.get(random.nextInt(images.size()));
    }

    private void validateCalendarExists(int calendarId) {
        if (!calendarRepository.existsById(calendarId)) {
            throw new CustomException(CALENDAR_NOT_FOUND);
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
