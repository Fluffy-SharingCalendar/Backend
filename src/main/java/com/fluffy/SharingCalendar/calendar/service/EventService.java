package com.fluffy.SharingCalendar.calendar.service;

import static com.fluffy.SharingCalendar.common.Constant.DEFAULT_PROFILE_IMAGE_URL;
import static com.fluffy.SharingCalendar.exception.ErrorCode.EVENT_NOT_FOUND;

import com.fluffy.SharingCalendar.calendar.domain.Calendar;
import com.fluffy.SharingCalendar.calendar.domain.Event;
import com.fluffy.SharingCalendar.calendar.dto.EventDto;
import com.fluffy.SharingCalendar.calendar.dto.response.EventDetailResponseDto;
import com.fluffy.SharingCalendar.calendar.dto.resquest.RegisterEventRequestDto;
import com.fluffy.SharingCalendar.calendar.dto.resquest.UpdateEventRequestDto;
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
    private final EventQDslRepository eventQDslRepository;

    @Transactional
    public void createEvent(RegisterEventRequestDto request, String loginId) {
        Calendar calendar = calendarService.checkAndFindCalendarById(request.getCalendarId(), loginId);
        List<User> participants = getUsersFromIds(request.getParticipantsIds());

        Event event = Event.create(request, calendar, participants);
        eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public List<EventDto> getEventsForCalendar(int calendarId, int year, int month, String loginId) {
        calendarService.checkAndFindCalendarById(calendarId, loginId);

        return eventQDslRepository.findEventsByCalendarAndMonth(calendarId, year, month).stream()
                .map(EventDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventDetailResponseDto getEventDetails(int eventId) {
        EventDto eventDto = eventRepository.findById(eventId)
                .map(EventDto::new)
                .orElseThrow(() -> new CustomException(EVENT_NOT_FOUND));

        URL url = getRandomImageForEvent(eventId);

        return new EventDetailResponseDto(eventDto, url);
    }

    @Transactional
    public void updateEvent(int eventId, UpdateEventRequestDto request, String loginId) {
        Event event = checkPermission(eventId, loginId);
        List<User> newParticipants = getUsersFromIds(request.getParticipantsIds());

        event.update(request, newParticipants);
    }

    @Transactional
    public void deleteEvent(int eventId, String loginId) {
        Event event = checkPermission(eventId, loginId);
        eventRepository.delete(event);
    }

    public URL getRandomImageForEvent(int eventId) {
        List<URL> images = eventQDslRepository.findImagesByEventId(eventId);

        if (images.isEmpty()) {
            return DEFAULT_PROFILE_IMAGE_URL;
        }

        Random random = new Random();
        return images.get(random.nextInt(images.size()));
    }

    private List<User> getUsersFromIds(List<Integer> participantIds) {
        return (participantIds == null || participantIds.isEmpty())
                ? List.of()
                : participantIds.stream()
                        .map(userService::findByUserId)
                        .collect(Collectors.toList());
    }

    private Event checkPermission(int eventId, String loginId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new CustomException(EVENT_NOT_FOUND));

        calendarService.checkAndFindCalendarById(event.getCalendar().getId(), loginId);

        return event;
    }
}
