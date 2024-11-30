package com.fluffy.SharingCalendar.service;

import com.fluffy.SharingCalendar.domain.Calendar;
import com.fluffy.SharingCalendar.dto.response.CalendarResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class CalendarServiceTest {

    @Autowired
    private CalendarService calendarService;

    @Test
    public void testSaveAndFindCalendar() {
        // given
        Calendar calendar = Calendar.builder()
                .name("fluffy")
                .profileImageUrl(null)
                .backgroundImageUrl(null)
                .createdAt(LocalDateTime.now())
                .build();

        // when
        Calendar savedCalendar = calendarService.saveCalendar(calendar);
        CalendarResponseDto foundCalendar = calendarService.findCalendarById(savedCalendar.getId());

        // then
        assertEquals(savedCalendar.getName(), foundCalendar.getName());
    }

}