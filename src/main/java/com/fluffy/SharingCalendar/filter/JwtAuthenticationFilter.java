package com.fluffy.SharingCalendar.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluffy.SharingCalendar.domain.User;
import com.fluffy.SharingCalendar.exception.CustomException;
import com.fluffy.SharingCalendar.exception.ErrorCode;
import com.fluffy.SharingCalendar.service.UserService;
import com.fluffy.SharingCalendar.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

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
                throw new CustomException(ErrorCode.INVALID_NICKNAME);
            }

            // 닉네임 중복 확인
            if (!userService.isNicknameAvailable(user.getNickname())) {
                throw new CustomException(ErrorCode.ALREADY_SAVED_DISPLAY);
            }

            // 사용자 저장 및 토큰 생성
            userService.save(user);
            String token = jwtUtil.generateToken(user);
            log.info("Generated Token: {}", token);

            // 응답 헤더에 토큰 추가
            response.setHeader("Authorization", "Bearer " + token);
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // 로그인 요청이 아닌 경우 JWT 토큰 검증
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 이후의 토큰만 추출
            try {
                // JWT 토큰 검증
                if (jwtUtil.isTokenValid(token)) {
                    String username = jwtUtil.getNickname(token);
                    User user = userService.findByNickname(username); // 토큰에서 사용자 정보 추출 및 로드
                    // SecurityContextHolder에 인증 정보 설정
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(
                                    user, null, Collections.emptyList()));
                }
            } catch (Exception e) {
                log.error("JWT 토큰 검증 실패: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid Token");
                return;
            }
        }

        filterChain.doFilter(request, response); // 다음 필터로 진행

    }

}

