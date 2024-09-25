package com.fluffy.SharingCalendar.model;

import jakarta.persistence.*;

@Entity
@Table(name="mvp_user")
public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "profile_image_index")
    private String profileImageIndex;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImageIndex() {
        return profileImageIndex;
    }

    public void setProfileImageIndex(String profileImageIndex) {
        this.profileImageIndex = profileImageIndex;
    }

}
