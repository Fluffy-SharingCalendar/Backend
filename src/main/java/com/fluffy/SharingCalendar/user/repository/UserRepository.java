package com.fluffy.SharingCalendar.user.repository;

import com.fluffy.SharingCalendar.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByLoginId(String loginId);
    Optional<User> findByLoginId(String loginId);
    List<User> findByNameContaining(String keyword);
}