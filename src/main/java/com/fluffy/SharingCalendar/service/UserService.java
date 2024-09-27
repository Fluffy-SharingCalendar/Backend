package com.fluffy.SharingCalendar.service;
import com.fluffy.SharingCalendar.dto.UserInfoDTO;
import com.fluffy.SharingCalendar.model.User;
import com.fluffy.SharingCalendar.repository.UserRepository;
import com.fluffy.SharingCalendar.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public boolean isNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    public boolean validateNickname(String nickname) {
        return nickname.length() >= 1 && nickname.length() <= 25; // 1~25자 확인
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User findByNickname(String nickname) {
        Optional<User> userOptional = userRepository.findByNickname(nickname);
        return userOptional.orElse(null);  // 사용자를 찾지 못하면 null 반환
    }

    public UserInfoDTO getUserInfo(String token) {
        String nickname = jwtUtil.getNickname(token);
        Optional<User> userOpt = userRepository.findByNickname(nickname);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return UserInfoDTO.builder()
                    .nickname(user.getNickname())
                    .phoneNumber(user.getPhoneNumber())
                    .profileImageIndex(user.getProfileImageIndex())
                    .build();
        }
        return null; // 사용자 정보가 없을 경우 null 반환
    }





}