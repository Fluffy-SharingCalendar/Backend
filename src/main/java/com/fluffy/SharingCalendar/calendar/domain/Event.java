package com.fluffy.SharingCalendar.calendar.domain;

import com.fluffy.SharingCalendar.calendar.dto.resquest.RegisterEventRequestDto;
import com.fluffy.SharingCalendar.calendar.dto.resquest.UpdateEventRequestDto;
import com.fluffy.SharingCalendar.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event")
@NoArgsConstructor
@Getter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private int eventId;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "color", nullable = false, length = 7)
    private String color;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    private Calendar calendar;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<EventParticipant> participants = new ArrayList<>();

    public Event(String title, String color, LocalDate startDate, LocalDate endDate, Calendar calendar,
            List<User> users) {
        this.title = title;
        this.color = color;
        this.startDate = startDate;
        this.endDate = endDate;
        this.calendar = calendar;
        setParticipants(users);
    }

    public static Event create(RegisterEventRequestDto request, Calendar calendar, List<User> participants) {
        return new Event(request.getTitle(), request.getColor(), request.getStartDate(), request.getEndDate(), calendar,
                participants);
    }

    public void update(UpdateEventRequestDto request, List<User> newParticipants) {
        if (request.getTitle() != null) {
            this.title = request.getTitle();
        }
        if (request.getStartDate() != null) {
            this.startDate = request.getStartDate();
        }
        if (request.getEndDate() != null) {
            this.endDate = request.getEndDate();
        }
        if (request.getColor() != null) {
            this.color = request.getColor();
        }

        if (newParticipants != null) {
            setParticipants(newParticipants);
        }
    }

    private void setParticipants(List<User> users) {
        this.participants.clear();
        users.forEach(this::addParticipant);
    }

    private void addParticipant(User user) {
        this.participants.add(new EventParticipant(this, user));
    }
}
