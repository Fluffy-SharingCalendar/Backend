package com.fluffy.SharingCalendar.service;
import com.fluffy.SharingCalendar.dto.UserInfoDto;
import com.fluffy.SharingCalendar.domain.User;
import com.fluffy.SharingCalendar.exception.CustomException;
import com.fluffy.SharingCalendar.repository.UserRepository;
import com.fluffy.SharingCalendar.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.fluffy.SharingCalendar.exception.ErrorCode.INVALID_NICKNAME;
import static com.fluffy.SharingCalendar.exception.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public boolean isNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
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

    // 토큰으로 사용자 정보 얻어오기
    public UserInfoDto getUserInfo(String token) {
        String nickname = jwtUtil.getNickname(token);
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        return UserInfoDto.builder()
                .nickname(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .profileImageIndex(user.getProfileImageIndex())
                .build();
    }





}