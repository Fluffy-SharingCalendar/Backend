package com.fluffy.SharingCalendar.user.service;

import com.fluffy.SharingCalendar.user.domain.User;
import com.fluffy.SharingCalendar.exception.CustomException;
import com.fluffy.SharingCalendar.user.repository.UserRepository;
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

    public boolean checkNickname(String nickname) {
        if (userRepository.existsByLoginId(nickname)) {
            throw new CustomException(ALREADY_SAVED_DISPLAY);
        }
        return true;
    }

    public boolean validateNickname(String nickname) {
        if (nickname.length() < 1 || nickname.length() > 25) {
            throw new CustomException(INVALID_NICKNAME);
        }
        return true;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public User findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

//    // 토큰으로 사용자 정보 얻어오기
//    public UserInfoDto getUserInfo(String token) {
//        String nickname = jwtUtil.getNickname(token);
//        User user = userRepository.findByNickname(nickname)
//                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
//
//        return UserInfoDto.builder()
//                .nickname(user.getNickname())
//                .phoneNumber(user.getPhoneNumber())
//                .profileImageIndex(user.getProfileImageIndex())
//                .build();
//    }


}