package com.fluffy.SharingCalendar.service;

import com.fluffy.SharingCalendar.domain.Event;
import com.fluffy.SharingCalendar.dto.EventDto;
import com.fluffy.SharingCalendar.dto.response.EventDetailResponseDto;
import com.fluffy.SharingCalendar.repository.CalendarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class EventServiceTest {

    @Autowired
    private EventService eventService;
    @Autowired
    private CalendarRepository calendarRepository;

    private Event testEvent;

    @BeforeEach
    void setup() {
        testEvent = Event.builder()
                .title("test")
                .color("#FF5733")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .calendar(calendarRepository.findById(1).orElseThrow(() -> new IllegalArgumentException("캘린더가 존재하지 않습니다.")))
                .build();
    }

    @Test
    public void SaveAndFindEvent() {
        //given

        //when
        Event savedEvent = eventService.saveEvent(testEvent);
        List<EventDto> findEvent = eventService.getEventsForCalendar(1);

        //then
        assertEquals(savedEvent.getEventId(),findEvent.get(findEvent.size() - 1).getEventId());
    }

    @Test
    public void getEventDetails() {
        //given
        Event savedEvent = eventService.saveEvent(testEvent);

        //when
        EventDetailResponseDto eventDetails = eventService.getEventDetails(savedEvent.getEventId());

        //then
        assertEquals(savedEvent.getTitle(), eventDetails.getTitle());
        assertEquals(savedEvent.getColor(), eventDetails.getColor());
        assertEquals(savedEvent.getStartDate(), eventDetails.getStartDate());
        assertEquals(savedEvent.getEndDate(), eventDetails.getEndDate());
    }

    @Test
    public void getEventDetailsWhenEventDoesNotExist() {
        //given
        // 존재하지 않는 이벤트 ID 사용

        //when & then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            eventService.getEventDetails(-1);
        });

        assertEquals("이벤트를 찾을 수 없습니다.", exception.getMessage());
    }
}