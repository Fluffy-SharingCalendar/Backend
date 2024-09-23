package com.fluffy.SharingCalendar.service;

import com.fluffy.SharingCalendar.domain.Event;
import com.fluffy.SharingCalendar.dto.EventDto;
import com.fluffy.SharingCalendar.repository.CalendarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class EventServiceTest {

    @Autowired
    private EventService eventService;
    @Autowired
    private CalendarRepository calendarRepository;

    @Test
    public void SaveAndFindEvent() {
        //given
        Event event = Event.builder()
                .title("방명록")
                .color("#0047AB")
                .startDate(LocalDateTime.of(2024,10,23,0,0))
                .endDate(LocalDateTime.now().plusDays(3))
                .calendar(calendarRepository.findById(1).get())
                .build();

        //when
        Event savedEvent = eventService.saveEvent(event);
        List<EventDto> findEvent = eventService.getEventsForCalendar(1);

        //then
        assertEquals(savedEvent.getEventId(),findEvent.get(findEvent.size() - 1).getEventId());
    }
}