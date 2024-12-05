package com.fluffy.SharingCalendar.service;

import com.fluffy.SharingCalendar.domain.User;
import com.fluffy.SharingCalendar.dto.UserInfoDto;
import com.fluffy.SharingCalendar.exception.CustomException;
import com.fluffy.SharingCalendar.repository.UserRepository;
import com.fluffy.SharingCalendar.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.fluffy.SharingCalendar.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public boolean checkLoginIdDuplicate(String loginId) {
        if (userRepository.existsByLoginId(loginId)) {
            throw new CustomException(ALREADY_SAVED_DISPLAY);
        }
        return true;
    }

    public boolean validateLoginId(String loginId) {
        if (loginId.length() < 1 || loginId.length() > 25) {
            throw new CustomException(INVALID_LOGIN_ID);
        }
        return true;
    }

    public boolean validateName(String name) {
        if (name.length() < 1 || name.length() > 25) {
            throw new CustomException(INVALID_NAME);
        }
        return true;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public User findByLoginId(String nickname) {
        return userRepository.findByLoginId(nickname)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    // 토큰으로 사용자 정보 얻어오기
    public UserInfoDto getUserInfo(String token) {
        String loginId = jwtUtil.getLoginId(token);
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        return UserInfoDto.builder()
                .name(user.getName())
                .loginId(user.getLoginId())
                .notificationStatus(user.isNotificationStatus())
                .build();
    }


}