package com.fluffy.SharingCalendar.controller;

import com.fluffy.SharingCalendar.dto.request.CheckNicknameRequestDto;
import com.fluffy.SharingCalendar.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/users")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/validation")
    public ResponseEntity<?> isNicknameAvailable(@RequestBody CheckNicknameRequestDto requestDto) {
        userService.checkNickname(requestDto.getNickname());
        return ResponseEntity.ok("사용 가능한 닉네임입니다.");
    }
}
