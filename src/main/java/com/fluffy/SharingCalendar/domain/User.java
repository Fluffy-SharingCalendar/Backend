package com.fluffy.SharingCalendar.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 20, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean notificationStatus = true;

    @Column(nullable = false, length = 1)
    private String isDeleted = "N";

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Profile> profiles = new ArrayList<>();

}
