package com.fluffy.SharingCalendar.user.controller;

import com.fluffy.SharingCalendar.user.dto.request.CheckLoginIdRequestDto;
import com.fluffy.SharingCalendar.user.dto.request.LoginRequestDto;
import com.fluffy.SharingCalendar.user.dto.request.RegisterUserRequestDto;
import com.fluffy.SharingCalendar.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
        userService.checkLoginIdDuplicated(requestDto.getLoginId());
        return ResponseEntity.ok(Collections.singletonMap("message", "사용 가능한 아이디입니다."));
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequestDto requestDto) {
        userService.registerUser(requestDto);
        return ResponseEntity.ok(Collections.singletonMap("message", "회원가입이 완료되었습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto) {
        String token = userService.login(requestDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
    }



}
