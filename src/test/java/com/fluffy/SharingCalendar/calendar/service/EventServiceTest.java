package com.fluffy.SharingCalendar.calendar.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fluffy.SharingCalendar.calendar.domain.Event;
import com.fluffy.SharingCalendar.calendar.domain.EventParticipant;
import com.fluffy.SharingCalendar.calendar.dto.resquest.UpdateEventRequestDto;
import com.fluffy.SharingCalendar.calendar.repository.CalendarRepository;
import com.fluffy.SharingCalendar.calendar.repository.EventParticipantRepository;
import com.fluffy.SharingCalendar.calendar.repository.EventRepository;
import com.fluffy.SharingCalendar.exception.CustomException;
import com.fluffy.SharingCalendar.user.domain.User;
import com.fluffy.SharingCalendar.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private EventParticipantRepository eventParticipantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @PersistenceContext
    private EntityManager em;

    private Event event;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .name("user0")
                .password("123!wkfnvjAk2r!")
                .isDeleted('N')
                .createdAt(LocalDateTime.now())
                .loginId("testUser").build();
        userRepository.save(testUser);

        int calendarId = calendarService.createCalendar("calendar", null, "testUser").getCalendarId();
        event = new Event("Original Title",
                "#FF0000",
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 2, 5),
                calendarRepository.findById(calendarId).get(),
                List.of());
        eventRepository.save(event);
        em.flush();
        em.clear();
    }

//    @Test
//    void 참여자_업데이트_테스트() {
//        // given
//        User user1 = User.builder()
//                .name("user1")
//                .password("123!wkfnvjAk2r")
//                .isDeleted('N')
//                .createdAt(LocalDateTime.now())
//                .loginId("testUser1").build();
//        User user2 = User.builder()
//                .name("user2")
//                .createdAt(LocalDateTime.now())
//                .password("123wkfnvjk2r")
//                .isDeleted('N')
//                .loginId("testUser2").build();
//        userRepository.save(user1);
//        userRepository.save(user2);
//
//        EventParticipant participant1 = new EventParticipant(1, event, user1);
//        eventParticipantRepository.save(participant1);
//
//        em.flush();
//        em.clear();
//
//        UpdateEventRequestDto updateRequest = UpdateEventRequestDto.builder()
//                .participantsIds(List.of(user1.getId(),user2.getId()))
//                .build();
//
//        // when
//        eventService.updateEvent(event.getEventId(), updateRequest, "testUser");
//
//        // then
//        List<EventParticipant> updatedParticipants = eventParticipantRepository.findByEvent(event);
//        assertThat(updatedParticipants).hasSize(2);
//    }

    @Test
    void 이벤트_정보_업데이트_테스트() {
        // given
        User user1 = User.builder()
                .name("user1")
                .password("123!wkfnvjAk2r")
                .isDeleted('N')
                .createdAt(LocalDateTime.now())
                .loginId("testUser1").build();
        User user2 = User.builder()
                .name("user2")
                .createdAt(LocalDateTime.now())
                .password("123wkfnvjk2r")
                .isDeleted('N')
                .loginId("testUser2").build();
        userRepository.save(user1);
        userRepository.save(user2);

        EventParticipant participant1 = new EventParticipant(event, user1);
        eventParticipantRepository.save(participant1);

        em.flush();
        em.clear();

        UpdateEventRequestDto updateRequest = UpdateEventRequestDto.builder()
                .title("Updated Title")
                .color("#00FF00")
                .startDate(LocalDate.of(2024, 2, 2))
                .endDate(LocalDate.of(2024, 2, 6))
                .participantsIds(List.of(user1.getId(), user2.getId()))
                .build();

        // when
        eventService.updateEvent(event.getEventId(), updateRequest, "testUser");

        // then
        Event updatedEvent = eventRepository.findById(event.getEventId()).orElseThrow();
        assertThat(updatedEvent.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedEvent.getColor()).isEqualTo("#00FF00");
        assertThat(updatedEvent.getStartDate()).isEqualTo(LocalDate.of(2024, 2, 2));
        assertThat(updatedEvent.getEndDate()).isEqualTo(LocalDate.of(2024, 2, 6));
        assertThat(updatedEvent.getParticipants()).hasSize(2);

        em.flush();
        em.clear();
    }

    @Test
    void 권한_없는_사용자의_업데이트_시도_예외_테스트() {
        // given
        UpdateEventRequestDto updateRequest = UpdateEventRequestDto.builder()
                .title("Unauthorized Update")
                .build();

        // when & then
        assertThatThrownBy(() -> eventService.updateEvent(event.getEventId(), updateRequest, "wrongUser"))
                .isInstanceOf(CustomException.class);
    }
}
