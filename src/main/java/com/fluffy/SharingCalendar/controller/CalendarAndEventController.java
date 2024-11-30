package com.fluffy.SharingCalendar.controller;

import com.fluffy.SharingCalendar.dto.EventDto;
import com.fluffy.SharingCalendar.dto.response.EventDetailResponseDto;
import com.fluffy.SharingCalendar.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class CalendarAndEventController {

    private final EventService eventService;

    @GetMapping("/calendars/{calendarId}/events")
    public ResponseEntity<List<EventDto>> readEventByCalendar(@PathVariable Integer calendarId) {
        return ResponseEntity.ok(eventService.getEventsForCalendar(calendarId));
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<EventDetailResponseDto> readEventDetail(@PathVariable Integer eventId) {
        return ResponseEntity.ok(eventService.getEventDetails(eventId));
    }
}
