package com.fluffy.SharingCalendar.user.service;

import com.fluffy.SharingCalendar.user.domain.SecurityAnswer;
import com.fluffy.SharingCalendar.user.domain.SecurityQuestion;
import com.fluffy.SharingCalendar.user.domain.User;
import com.fluffy.SharingCalendar.user.dto.SecurityAnswerDto;
import com.fluffy.SharingCalendar.user.dto.UserInfoDto;
import com.fluffy.SharingCalendar.user.dto.request.LoginRequestDto;
import com.fluffy.SharingCalendar.user.dto.request.RegisterUserRequestDto;
import com.fluffy.SharingCalendar.exception.CustomException;
import com.fluffy.SharingCalendar.user.repository.SecurityAnswerRepository;
import com.fluffy.SharingCalendar.user.repository.SecurityQuestionRepository;
import com.fluffy.SharingCalendar.user.repository.UserRepository;
import com.fluffy.SharingCalendar.util.JwtUtil;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.fluffy.SharingCalendar.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SecurityQuestionRepository securityQuestionRepository;
    private final SecurityAnswerRepository securityAnswerRepository;
    private final JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public boolean checkLoginIdDuplicated(String loginId) {
        if (userRepository.existsByLoginId(loginId)) {
            throw new CustomException(ALREADY_SAVED_DISPLAY);
        }
        return true;
    }

    @Transactional
    public boolean validateLoginId(String loginId) {
        if (loginId.length() < 1 || loginId.length() > 25) {
            throw new CustomException(INVALID_LOGIN_ID);
        }
        return true;
    }

    @Transactional
    public boolean validateName(String name) {
        if (name.length() < 1 || name.length() > 25) {
            throw new CustomException(INVALID_NAME);
        }
        return true;
    }

    private boolean validatePassword(String password) {
        // 비밀번호 유효성 검사: 8자 이상, 영문(대문자/소문자), 숫자, 특수기호 포함
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        if (!matcher.matches()) {
            throw new CustomException(INVALID_PASSWORD);
        }
        return true;
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public User findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    @Transactional
    public User findByUserId(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    @Transactional
    public UserInfoDto getUserInfo(String token) {
        String loginId = jwtUtil.getLoginId(token);

        User user = findByLoginId(loginId);

        return UserInfoDto.builder()
                .name(user.getName())
                .loginId(user.getLoginId())
                .notificationStatus(user.isNotificationStatus())
                .build();
    }

    @Transactional
    public void registerUser(RegisterUserRequestDto requestDto) {
        validateRegistrationData(requestDto);

        User user = createUser(requestDto);
        userRepository.save(user);

        List<SecurityAnswer> securityAnswers = createSecurityAnswers(requestDto.getSecurityAnswers(), user);
        securityAnswerRepository.saveAll(securityAnswers);
    }

    private void validateRegistrationData(RegisterUserRequestDto requestDto){
        validateName(requestDto.getName());
        validateLoginId(requestDto.getLoginId());
        validatePassword(requestDto.getPassword());
        checkLoginIdDuplicated(requestDto.getLoginId());
    }

    private User createUser(RegisterUserRequestDto requestDto) {

        User user = User.builder()
                .name(requestDto.getName())
                .loginId(requestDto.getLoginId())
                .password(requestDto.getPassword())  // 비밀번호는 평문으로 전달
                .notificationStatus(true)
                .isDeleted('N')
                .build();

        user.hashPassword(passwordEncoder);

        return user;
    }

    private List<SecurityAnswer> createSecurityAnswers(List<SecurityAnswerDto> securityAnswerDtos, User user) {
        return securityAnswerDtos.stream()
                .map(securityAnswerDto -> {
                    SecurityQuestion securityQuestion = findSecurityQuestion(securityAnswerDto.questionId());
                    return createSecurityAnswer(securityAnswerDto, user, securityQuestion);
                })
                .collect(Collectors.toList());
    }

    private SecurityQuestion findSecurityQuestion(Integer questionId) {
        return securityQuestionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(SECURITY_QUESTION_NOT_FOUND));
    }

    private SecurityAnswer createSecurityAnswer(SecurityAnswerDto securityAnswerDto, User user, SecurityQuestion securityQuestion) {
        return SecurityAnswer.builder()
                .answer(securityAnswerDto.answer())
                .user(user)
                .question(securityQuestion)
                .build();
    }

    public String login(LoginRequestDto requestDto) {
        User user = findByLoginId(requestDto.getLoginId());
        if(!user.checkPassword(requestDto.getPassword(), passwordEncoder)){
            throw new CustomException(INVALID_CREDENTIALS);
        }
        return jwtUtil.generateToken(user);
    }
}