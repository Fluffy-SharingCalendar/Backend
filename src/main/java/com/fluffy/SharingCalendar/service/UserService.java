package com.fluffy.SharingCalendar.service;
import com.fluffy.SharingCalendar.dto.UserInfoDTO;
import com.fluffy.SharingCalendar.model.User;
import com.fluffy.SharingCalendar.repository.UserRepository;
import com.fluffy.SharingCalendar.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User findByNickname(String nickname) {
        Optional<User> userOptional = userRepository.findByNickname(nickname);
        return userOptional.orElse(null);  // 사용자를 찾지 못하면 null 반환
    }

    public UserInfoDTO getUserInfoFromToken(String token) {
        String nickname = jwtUtil.getNickname(token);
        Optional<User> userOpt = userRepository.findByNickname(nickname);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return new UserInfoDTO(user.getNickname(), user.getPhoneNumber(), user.getProfileImageIndex()); // profileImageIndex 추가
        }
        return null; // 사용자 정보가 없을 경우 null 반환
    }


}