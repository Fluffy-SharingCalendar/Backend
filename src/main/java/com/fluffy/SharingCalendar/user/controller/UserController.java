package com.fluffy.SharingCalendar.user.controller;

import com.fluffy.SharingCalendar.user.dto.UserInfoDto;
import com.fluffy.SharingCalendar.user.dto.request.CheckLoginIdRequestDto;
import com.fluffy.SharingCalendar.user.dto.request.LoginRequestDto;
import com.fluffy.SharingCalendar.user.dto.request.RegisterUserRequestDto;
import com.fluffy.SharingCalendar.user.dto.request.VerifyUserIdentityRequestDto;
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

    @GetMapping
    @ResponseBody
    public ResponseEntity<UserInfoDto> getUserInfo(@RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        UserInfoDto userInfo = userService.getUserInfo(jwtToken);
        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/verification")
    public ResponseEntity<?> verifyUserIdentity(@RequestBody VerifyUserIdentityRequestDto requestDto, @RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        boolean isVerified = userService.verifySecurityAnswer(jwtToken, requestDto);

        return isVerified ? ResponseEntity.ok().build() : ResponseEntity.status(400).build();
    }

}
