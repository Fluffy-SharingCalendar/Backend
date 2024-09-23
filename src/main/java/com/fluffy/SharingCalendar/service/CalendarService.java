package com.fluffy.SharingCalendar.service;

import com.fluffy.SharingCalendar.domain.Calendar;
import com.fluffy.SharingCalendar.repository.CalendarRepository;
import com.fluffy.SharingCalendar.dto.CalendarResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    @Autowired
    private final CalendarRepository calendarRepository;

    private static final String DEFAULT_PROFILE_IMAGE_URL = "defaultProfileImageUrl";

    public Calendar saveCalendar(Calendar calendar) {
        return calendarRepository.save(calendar);
    }

    @Transactional(readOnly = true)
    public CalendarResponseDto findCalendarById(int calendarId) {
        return calendarRepository.findById(calendarId)
                .map(this::convertDto)
                .orElseThrow(() -> new IllegalArgumentException("캘린더를 찾을 수 없습니다."));
    }

    private CalendarResponseDto convertDto(Calendar calendar) {
        return CalendarResponseDto.builder()
                .name(calendar.getName())
                .profileImageUrl(calendar.getProfileImageUrl() != null ? calendar.getProfileImageUrl() : DEFAULT_PROFILE_IMAGE_URL)
                .backgroundImage(calendar.getBackgroundImageUrl())
                .build();
    }

}
