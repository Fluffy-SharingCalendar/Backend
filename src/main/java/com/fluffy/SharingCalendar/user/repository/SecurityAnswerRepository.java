package com.fluffy.SharingCalendar.user.repository;

import com.fluffy.SharingCalendar.user.domain.SecurityAnswer;
import com.fluffy.SharingCalendar.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityAnswerRepository extends JpaRepository<SecurityAnswer, Long> {
    Optional<SecurityAnswer> findByUserAndQuestionId(User user, Integer questionId);
}

