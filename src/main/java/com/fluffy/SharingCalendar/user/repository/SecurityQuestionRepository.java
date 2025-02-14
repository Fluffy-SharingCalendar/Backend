package com.fluffy.SharingCalendar.user.repository;

import com.fluffy.SharingCalendar.user.domain.SecurityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityQuestionRepository extends JpaRepository<SecurityQuestion, Integer> {

}

