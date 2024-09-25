package com.fluffy.SharingCalendar.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
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
