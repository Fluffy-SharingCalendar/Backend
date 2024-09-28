package com.fluffy.SharingCalendar.service;

import com.fluffy.SharingCalendar.domain.Calendar;
import com.fluffy.SharingCalendar.dto.response.CalendarResponseDto;
import com.fluffy.SharingCalendar.repository.CalendarRepository;
import com.fluffy.SharingCalendar.util.ConstantUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;

@Service
@RequiredArgsConstructor
public class CalendarService {

    public static final URL DEFAULT_PROFILE_IMAGE_URL = ConstantUtil.getRandomDefaultImageUrl();
    @Autowired
    private final CalendarRepository calendarRepository;

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
                .profileImageUrl(calendar.getProfileImageUrl() != null ? calendar.getProfileImageUrl() : String.valueOf(DEFAULT_PROFILE_IMAGE_URL))
                .backgroundImage(calendar.getBackgroundImageUrl())
                .build();
    }

}
