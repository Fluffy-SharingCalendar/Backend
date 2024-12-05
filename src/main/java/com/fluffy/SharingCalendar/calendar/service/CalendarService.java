package com.fluffy.SharingCalendar.calendar.service;

import static com.fluffy.SharingCalendar.common.Constant.DEFAULT_PROFILE_IMAGE_URL;
import static com.fluffy.SharingCalendar.exception.ErrorCode.ALREADY_INVITED_USER;
import static com.fluffy.SharingCalendar.exception.ErrorCode.CALENDAR_MEMBER_NOT_FOUND;
import static com.fluffy.SharingCalendar.exception.ErrorCode.CALENDAR_NOT_FOUND;

import com.fluffy.SharingCalendar.calendar.domain.Calendar;
import com.fluffy.SharingCalendar.calendar.domain.CalendarMember;
import com.fluffy.SharingCalendar.calendar.dto.response.CalendarMemberResponseDto;
import com.fluffy.SharingCalendar.calendar.dto.response.CalendarResponseDto;
import com.fluffy.SharingCalendar.calendar.dto.response.RegisterCalendarResponseDto;
import com.fluffy.SharingCalendar.calendar.repository.CalendarMemberRepository;
import com.fluffy.SharingCalendar.calendar.repository.CalendarRepository;
import com.fluffy.SharingCalendar.common.image.S3Service;
import com.fluffy.SharingCalendar.exception.CustomException;
import com.fluffy.SharingCalendar.user.domain.User;
import com.fluffy.SharingCalendar.user.service.UserService;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final CalendarMemberRepository calendarMemberRepository;
    private final S3Service s3Service;
    private final UserService userService;

    @Transactional
    public RegisterCalendarResponseDto createCalendar(String name, MultipartFile profileImage, String loginId) {
        User user = userService.findByLoginId(loginId);
        URL profileImageUrl =
                isImage(profileImage) ? s3Service.uploadProfileImage(profileImage) : DEFAULT_PROFILE_IMAGE_URL;

        Calendar calendar = saveCalendar(name, profileImageUrl);
        saveCalendarMember(calendar, user);

        return new RegisterCalendarResponseDto(calendar.getId());
    }

    @Transactional(readOnly = true)
    public CalendarResponseDto findCalendarById(int calendarId) {
        return new CalendarResponseDto(findByCalendarId(calendarId));
    }

    @Transactional
    public RegisterCalendarResponseDto updateCalendar(Integer calendarId, String newName,
            MultipartFile newProfileImage) {
        Calendar calendar = findByCalendarId(calendarId);

        calendar.changeName(newName);
        changeProfileImage(newProfileImage, calendar);
        calendarRepository.save(calendar);

        return new RegisterCalendarResponseDto(calendarId);
    }

    @Transactional
    public void leaveCalendar(Integer calendarId, String loginId) {
        User user = userService.findByLoginId(loginId);

        CalendarMember member = checkUserIncluded(calendarId, user.getId());
        Calendar calendar = member.getCalendar();
        calendarMemberRepository.delete(member);

        if (calendar.isEmpty()) {
            calendarRepository.delete(calendar);
        }
    }

    @Transactional
    public void inviteUserToCalendar(int calendarId, int userId) {
        Calendar calendar = findByCalendarId(calendarId);
        User user = userService.findByUserId(userId);

        checkInvitation(calendarId, userId);

        CalendarMember member = CalendarMember.builder()
                .calendar(calendar)
                .userId(user.getId())
                .profileName(user.getName())
                .profileImageUrl(DEFAULT_PROFILE_IMAGE_URL)
                .status("invited")
                .build();

        calendarMemberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public List<CalendarMemberResponseDto> getCalendarMembers(int calendarId) {
        Calendar calendar = findByCalendarId(calendarId);
        return calendarMemberRepository.findByCalendarId(calendar.getId())
                .stream().map(CalendarMemberResponseDto::new)
                .toList();
    }

    private Calendar findByCalendarId(int calendarId) {
        return calendarRepository.findById(calendarId)
                .orElseThrow(() -> new CustomException(CALENDAR_NOT_FOUND));
    }

    private boolean isImage(MultipartFile file) {
        return file != null && !file.isEmpty();
    }

    private Calendar saveCalendar(String name, URL profileImageUrl) {
        Calendar calendar = Calendar.builder()
                .name(name)
                .profileImageUrl(profileImageUrl)
                .build();

        return calendarRepository.save(calendar);
    }

    private void saveCalendarMember(Calendar calendar, User user) {
        CalendarMember calendarMember = CalendarMember.builder()
                .calendar(calendar)
                .userId(user.getId())
                .profileName(user.getName())
                .profileImageUrl(DEFAULT_PROFILE_IMAGE_URL)
                .status("accepted")
                .invitedAt(LocalDateTime.now())
                .build();

        calendarMemberRepository.save(calendarMember);
    }

    private void changeProfileImage(MultipartFile newProfileImage, Calendar calendar) {
        if (isImage(newProfileImage)) {
            s3Service.deleteImage(calendar.getProfileImageUrl());
            URL profileImageUrl = s3Service.uploadProfileImage(newProfileImage);
            calendar.changeProfileImage(profileImageUrl);

            calendarRepository.save(calendar);
        }
    }

    private void checkInvitation(int calendarId, int userId) {
        checkUserIncluded(calendarId, userId);
        checkDuplicateInvitation(calendarId, userId);
    }

    private CalendarMember checkUserIncluded(Integer calendarId, Integer userId) {
        return calendarMemberRepository.findByCalendarIdAndUserId(calendarId, userId)
                .orElseThrow(() -> new CustomException(CALENDAR_MEMBER_NOT_FOUND));
    }

    private void checkDuplicateInvitation(Integer calendarId, Integer userId) {
        calendarMemberRepository.findByCalendarIdAndUserId(calendarId, userId).ifPresent(member -> {
            throw new CustomException(ALREADY_INVITED_USER);
        });
    }
}
