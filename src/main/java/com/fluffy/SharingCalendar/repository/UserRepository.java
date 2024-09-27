package com.fluffy.SharingCalendar.repository;

import com.fluffy.SharingCalendar.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByNickname(String nickname);
    Optional<User> findByNickname(String nickname);
}