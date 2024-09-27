package com.fluffy.SharingCalendar.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluffy.SharingCalendar.model.User;
import com.fluffy.SharingCalendar.service.UserService;
import com.fluffy.SharingCalendar.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        if ("POST".equalsIgnoreCase(request.getMethod()) && request.getRequestURI().equals("/login")) {
            String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
            User user = new ObjectMapper().readValue(body, User.class);

            // 닉네임 길이 유효성 검사
            if (!userService.validateNickname(user.getNickname())) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  // 상태 400 설정
                response.getWriter().write("닉네임은 1~25자 사이여야 합니다.");
                return;
            }

            // 닉네임 중복 확인
            if (userService.isNicknameAvailable(user.getNickname())) {
                userService.save(user);  // 사용자 저장
                String token = jwtUtil.generateToken(user);  // 새로운 토큰 생성
                log.info(token);

                // 응답 헤더에 토큰 추가
                response.setHeader("Authorization", "Bearer " + token);
                response.setStatus(HttpServletResponse.SC_OK);  // 상태 200 설정
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);  // 상태 409 설정
                response.getWriter().write("이미 존재하는 아이디입니다.");
            }
            return; // 더 이상 필터 체인에서 진행하지 않음
        }
        filterChain.doFilter(request, response); // 다음 필터로 진행

    }

}

