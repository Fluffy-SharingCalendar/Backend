package com.fluffy.SharingCalendar.calendar.domain;

import static com.fluffy.SharingCalendar.exception.ErrorCode.INVALID_CALENDAR_NAME;

import com.fluffy.SharingCalendar.exception.CustomException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
    private URL backgroundImageUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CalendarMember> members = new ArrayList<>();

    @Builder
    public Calendar(String name, URL profileImageUrl, URL backgroundImageUrl) {
        validateName(name);
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.backgroundImageUrl = backgroundImageUrl;
        this.createdAt = LocalDateTime.now();
    }

    private void validateName(String name) {
        if (name.isBlank() || name.length() <= 1 || name.length() > 25) {
            throw new CustomException(INVALID_CALENDAR_NAME);
        }
    }

    public void changeName(String newName) {
        if (newName != null && !newName.isBlank()) {
            this.name = newName;
        }
    }

    public void changeProfileImage(URL newProfileImageUrl) {
        if (newProfileImageUrl != null) {
            this.profileImageUrl = newProfileImageUrl;
        }
    }

    public boolean isEmpty() {
        return members.isEmpty();
    }
}
