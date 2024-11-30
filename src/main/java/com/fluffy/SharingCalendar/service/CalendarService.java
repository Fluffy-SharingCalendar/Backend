package com.fluffy.SharingCalendar.service;

import static com.fluffy.SharingCalendar.exception.ErrorCode.CALENDAR_NOT_FOUND;

import com.fluffy.SharingCalendar.config.S3Config;
import com.fluffy.SharingCalendar.domain.Calendar;
import com.fluffy.SharingCalendar.dto.response.CalendarResponseDto;
import com.fluffy.SharingCalendar.exception.CustomException;
import com.fluffy.SharingCalendar.repository.CalendarRepository;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    public static final URL DEFAULT_PROFILE_IMAGE_URL = S3Config.getDefaultImage();

    @Autowired
    private final CalendarRepository calendarRepository;

    public Calendar saveCalendar(Calendar calendar) {
        return calendarRepository.save(calendar);
    }

    @Transactional(readOnly = true)
    public CalendarResponseDto findCalendarById(int calendarId) {
        return calendarRepository.findById(calendarId)
                .map(this::convertDto)
                .orElseThrow(() -> new CustomException(CALENDAR_NOT_FOUND));
    }

    private CalendarResponseDto convertDto(Calendar calendar) {
        return CalendarResponseDto.builder()
                .name(calendar.getName())
                .profileImageUrl(calendar.getProfileImageUrl() != null ? calendar.getProfileImageUrl()
                                : String.valueOf(DEFAULT_PROFILE_IMAGE_URL))
                .backgroundImage(calendar.getBackgroundImageUrl())
                .build();
    }

}
