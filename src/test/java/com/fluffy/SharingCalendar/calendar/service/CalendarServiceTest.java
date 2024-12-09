package com.fluffy.SharingCalendar.calendar.service;

import static com.fluffy.SharingCalendar.exception.ErrorCode.ALREADY_INVITED_USER;
import static com.fluffy.SharingCalendar.exception.ErrorCode.CALENDAR_MEMBER_NOT_FOUND;
import static com.fluffy.SharingCalendar.exception.ErrorCode.CALENDAR_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.fluffy.SharingCalendar.calendar.domain.Calendar;
import com.fluffy.SharingCalendar.calendar.domain.CalendarMember;
import com.fluffy.SharingCalendar.calendar.dto.response.CalendarResponseDto;
import com.fluffy.SharingCalendar.calendar.dto.response.RegisterCalendarResponseDto;
import com.fluffy.SharingCalendar.calendar.repository.CalendarMemberRepository;
import com.fluffy.SharingCalendar.calendar.repository.CalendarRepository;
import com.fluffy.SharingCalendar.common.image.S3Service;
import com.fluffy.SharingCalendar.exception.CustomException;
import com.fluffy.SharingCalendar.user.domain.User;
import com.fluffy.SharingCalendar.user.repository.UserRepository;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@Transactional
public class CalendarServiceTest {

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private CalendarMemberRepository calendarMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock(lenient = true)
    private S3Service s3Service;

    @Autowired
    private CalendarService calendarService;

    private User user;
    private Calendar calendar;

    @BeforeEach
    public void setUp() {
        user = userRepository.save(User.builder()
                .loginId("testUser")
                .name("Test User")
                .createdAt(LocalDateTime.now())
                .notificationStatus(true)
                .isDeleted('N')
                .password("Password1234!")
                .build());

        calendar = calendarRepository.save(Calendar.builder()
                .name("Test Calendar")
                .profileImageUrl(createURL("http://image.url"))
                .build());

        calendarMemberRepository.save(CalendarMember.builder().calendar(calendar).userId(user.getId())
                .profileName(user.getName()).profileImageUrl(createURL("http://image.url"))
                .status("accepted").build());
    }

    @Test
    public void 정상적으로_캘린더_생성() throws Exception {
        // Given
        when(s3Service.uploadProfileImage(any(MultipartFile.class))).thenReturn(createURL("http://newimage.url"));

        // When
        RegisterCalendarResponseDto response = calendarService.createCalendar("New Calendar", null, "testUser");

        // Then
        assertNotNull(response);
        Calendar savedCalendar = calendarRepository.findById(response.getCalendarId()).orElseThrow();
        assertEquals("New Calendar", savedCalendar.getName());
    }

    @Test
    public void 정상적으로_캘린더_조회() {
        // Given

        // When
        CalendarResponseDto response = calendarService.findCalendarInfoById(calendar.getId(), "testUser");

        // Then
        assertNotNull(response);
        assertEquals(calendar.getName(), response.getName());
    }

    @Test
    public void 캘린더_조회_회원이_아닌_사용자() {
        // Given
        user = userRepository.save(User.builder()
                .loginId("nonMemberUser")
                .name("Test User")
                .createdAt(LocalDateTime.now())
                .notificationStatus(true)
                .isDeleted('N')
                .password("Password1234!")
                .build());

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            calendarService.findCalendarInfoById(calendar.getId(), "nonMemberUser");
        });
        assertEquals(CALENDAR_MEMBER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    public void 캘린더_조회_없는_캘린더() {
        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            calendarService.findCalendarInfoById(99, "testUser");
        });
        assertEquals(CALENDAR_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    public void 캘린더_수정_정상() throws Exception {
        // Given
        when(s3Service.uploadProfileImage(any(MultipartFile.class))).thenReturn(createURL("http://updatedimage.url"));

        // When
        RegisterCalendarResponseDto response = calendarService.updateCalendar(calendar.getId(), "Updated Calendar", null,
                "testUser");

        // Then
        assertNotNull(response);
        Calendar updatedCalendar = calendarRepository.findById(response.getCalendarId()).orElseThrow();
        assertEquals("Updated Calendar", updatedCalendar.getName());
    }

    @Test
    public void 일정_탈퇴_정상() {
        // Given

        // When
        calendarService.leaveCalendar(calendar.getId(), "testUser");

        // Then
        assertTrue(calendarMemberRepository.findByCalendarIdAndUserId(calendar.getId(), user.getId()).isEmpty());
    }

    @Test
    public void 초대_중복_초대() {
        // Given

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            calendarService.inviteUserToCalendar(calendar.getId(), user.getId(), "testUser");
        });
        assertEquals(ALREADY_INVITED_USER, exception.getErrorCode());
    }

    @Test
    public void 초대_없는_사용자() {
        // Given
        User invitedUser = userRepository.save(User.builder()
                .loginId("invitedUser")
                .name("Test User2")
                .createdAt(LocalDateTime.now())
                .notificationStatus(true)
                .isDeleted('N')
                .password("Password1234!")
                .build());

        // When
        calendarService.inviteUserToCalendar(calendar.getId(), invitedUser.getId(), "testUser");

        // Then
        assertNotNull(calendarMemberRepository.findByCalendarIdAndUserId(calendar.getId(), invitedUser.getId()).orElseThrow());
    }

    private URL createURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
