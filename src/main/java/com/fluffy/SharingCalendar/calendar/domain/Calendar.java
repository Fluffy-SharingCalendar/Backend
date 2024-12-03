package com.fluffy.SharingCalendar.calendar.domain;

import jakarta.persistence.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@Table(name = "calendar")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendar_id")
    private Integer id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "profile_image_url")
    private URL profileImageUrl;

    @Column(name = "background_image_url")
    private String backgroundImageUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CalendarMember> members = new ArrayList<>();

    public void addMember(CalendarMember member) {
        members.add(member);
        member.setCalendar(this);
    }

    public void removeMember(CalendarMember member) {
        members.remove(member);
        member.setCalendar(null);
    }
}
