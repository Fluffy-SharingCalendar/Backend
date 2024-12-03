package com.fluffy.SharingCalendar.calendar.service;

import static com.fluffy.SharingCalendar.exception.ErrorCode.CALENDAR_NOT_FOUND;

import com.fluffy.SharingCalendar.calendar.domain.Calendar;
import com.fluffy.SharingCalendar.calendar.domain.CalendarMember;
import com.fluffy.SharingCalendar.calendar.dto.response.CalendarResponseDto;
import com.fluffy.SharingCalendar.calendar.dto.response.RegisterCalendarResponseDto;
import com.fluffy.SharingCalendar.calendar.repository.CalendarMemberRepository;
import com.fluffy.SharingCalendar.calendar.repository.CalendarRepository;
import com.fluffy.SharingCalendar.config.S3Config;
import com.fluffy.SharingCalendar.exception.CustomException;
import com.fluffy.SharingCalendar.image.S3Service;
import com.fluffy.SharingCalendar.user.domain.User;
import com.fluffy.SharingCalendar.user.service.UserService;
import java.net.URL;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CalendarService {

    public static final URL DEFAULT_PROFILE_IMAGE_URL = S3Config.getDefaultImage();

    private final CalendarRepository calendarRepository;
    private final CalendarMemberRepository calendarMemberRepository;
    private final S3Service s3Service;
    private final UserService userService;

    @Transactional
    public RegisterCalendarResponseDto createCalendar(String name, MultipartFile profileImage, String userId) {

        User user = userService.findByLoginId(userId);
        URL profileImageUrl =
                profileImage != null ? s3Service.uploadProfileImage(profileImage) : DEFAULT_PROFILE_IMAGE_URL;

        Calendar calendar = saveCalendar(name, profileImageUrl);
        saveCalendarMember(calendar, user.getId());

        return new RegisterCalendarResponseDto(calendar.getId());
    }

    @Transactional(readOnly = true)
    public CalendarResponseDto findCalendarById(int calendarId) {
        return calendarRepository.findById(calendarId)
                .map(this::convertDto)
                .orElseThrow(() -> new CustomException(CALENDAR_NOT_FOUND));
    }

    private Calendar saveCalendar(String name, URL profileImageUrl) {
        Calendar calendar = Calendar.builder()
                .name(name)
                .profileImageUrl(profileImageUrl)
                .build();

        return calendarRepository.save(calendar);
    }

    private void saveCalendarMember(Calendar calendar, Integer userId) {
        CalendarMember calendarMember = CalendarMember.builder()
                .calendar(calendar)
                .userId(userId)
                .status("accepted")
                .invitedAt(LocalDateTime.now())
                .build();

        calendarMemberRepository.save(calendarMember);
    }

    private CalendarResponseDto convertDto(Calendar calendar) {
        return CalendarResponseDto.builder()
                .name(calendar.getName())
                .profileImageUrl(calendar.getProfileImageUrl() != null ? calendar.getProfileImageUrl()
                                : DEFAULT_PROFILE_IMAGE_URL)
                .backgroundImage(calendar.getBackgroundImageUrl())
                .build();
    }
}
