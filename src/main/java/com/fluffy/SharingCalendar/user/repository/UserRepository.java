package com.fluffy.SharingCalendar.user.repository;

import com.fluffy.SharingCalendar.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByLoginId(String loginId);

    Optional<User> findByLoginId(String loginId);
}