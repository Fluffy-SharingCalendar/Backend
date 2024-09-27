package com.fluffy.SharingCalendar.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name="mvp_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "profile_image_index")
    private int profileImageIndex;

}
