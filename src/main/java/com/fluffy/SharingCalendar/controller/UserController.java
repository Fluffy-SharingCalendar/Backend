package com.fluffy.SharingCalendar.controller;

import com.fluffy.SharingCalendar.dto.request.CheckLoginIdRequestDto;
import com.fluffy.SharingCalendar.dto.request.RegisterUserRequestDto;
import com.fluffy.SharingCalendar.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RequestMapping("/api/users")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/validation")
    public ResponseEntity<?> isLoginIdAvailable(@RequestBody CheckLoginIdRequestDto requestDto) {
        userService.validateLoginId(requestDto.getLoginId());
        userService.checkLoginIdDuplicate(requestDto.getLoginId());
        return ResponseEntity.ok(Collections.singletonMap("message", "사용 가능한 아이디입니다."));
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequestDto requestDto) {
        userService.validateName(requestDto.getName());
        userService.validatePassword(requestDto.getPassword());
        userService.checkLoginIdDuplicate(requestDto.getLoginId());
        userService.registerUser(requestDto);
        return ResponseEntity.ok(Collections.singletonMap("message", "회원가입이 완료되었습니다."));
    }

}
