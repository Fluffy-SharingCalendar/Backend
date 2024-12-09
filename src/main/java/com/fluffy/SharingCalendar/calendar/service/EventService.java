package com.fluffy.SharingCalendar.calendar.service;

import static com.fluffy.SharingCalendar.common.Constant.DEFAULT_PROFILE_IMAGE_URL;
import static com.fluffy.SharingCalendar.exception.ErrorCode.CALENDAR_NOT_FOUND;
import static com.fluffy.SharingCalendar.exception.ErrorCode.EVENT_NOT_FOUND;

import com.fluffy.SharingCalendar.calendar.domain.Calendar;
import com.fluffy.SharingCalendar.calendar.domain.Event;
import com.fluffy.SharingCalendar.calendar.domain.EventParticipant;
import com.fluffy.SharingCalendar.calendar.dto.EventDto;
import com.fluffy.SharingCalendar.calendar.dto.response.EventDetailResponseDto;
import com.fluffy.SharingCalendar.calendar.dto.resquest.RegisterEventRequestDto;
import com.fluffy.SharingCalendar.calendar.repository.CalendarRepository;
import com.fluffy.SharingCalendar.calendar.repository.EventParticipantRepository;
import com.fluffy.SharingCalendar.calendar.repository.EventQDslRepository;
import com.fluffy.SharingCalendar.calendar.repository.EventRepository;
import com.fluffy.SharingCalendar.exception.CustomException;
import com.fluffy.SharingCalendar.user.domain.User;
import com.fluffy.SharingCalendar.user.service.UserService;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final CalendarService calendarService;
    private final UserService userService;
    private final EventRepository eventRepository;
    private final EventParticipantRepository eventParticipantRepository;
    private final EventQDslRepository eventQDslRepository;
    private final CalendarRepository calendarRepository;

    @Transactional
    public void createEvent(RegisterEventRequestDto request, String loginId) {
        Calendar calendar = calendarService.checkAndFindCalendarById(request.getCalendarId(), loginId);

        Event event = request.toEvent(calendar);
        Event savedEvent = eventRepository.save(event);

        saveParticipant(savedEvent, request.getParticipantsIds());

    }

    @Transactional(readOnly = true)
    public List<EventDto> getEventsForCalendar(int calendarId, int year, int month) {
        validateCalendarExists(calendarId);

        return eventQDslRepository.findEventsByCalendarAndMonth(calendarId, year, month).stream()
                .map(EventDto :: new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventDetailResponseDto getEventDetails(int eventId) {
        EventDto eventDto = eventRepository.findById(eventId)
                .map(EventDto :: new)
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

    private void saveParticipant(Event event, List<Integer> participantIds) {
        if (participantIds != null && !participantIds.isEmpty()) {
            participantIds.forEach(userId -> {
                User user = userService.findByUserId(userId);
                EventParticipant participant = EventParticipant.builder()
                        .event(event)
                        .user(user)
                        .build();
                eventParticipantRepository.save(participant);
            });
        }
    }

    private void validateCalendarExists(int calendarId) {
        if (!calendarRepository.existsById(calendarId)) {
            throw new CustomException(CALENDAR_NOT_FOUND);
        }
    }

}
