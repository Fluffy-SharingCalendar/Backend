package com.fluffy.SharingCalendar.service;

import com.fluffy.SharingCalendar.model.User;
import com.fluffy.SharingCalendar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public User loadUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new UsernameNotFoundException(nickname));
    }
}
