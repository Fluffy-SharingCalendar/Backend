package com.fluffy.SharingCalendar.domain;

import jakarta.persistence.*;
import lombok.*;

import java.net.URL;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@Table(name = "post_image")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private int id;

    @Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
    private URL imageUrl;

    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
