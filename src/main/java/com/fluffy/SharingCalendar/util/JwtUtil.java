package com.fluffy.SharingCalendar.util;

import com.fluffy.SharingCalendar.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Log4j2
@Component
public class JwtUtil {
    private static final long JWT_TOKEN_VALIDITY_SECONDS = 60 * 60 * 12;
    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(User user) {
        String token = Jwts.builder()
                .setSubject(user.getNickname())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY_SECONDS * 1000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        // 생성된 토큰을 로그로 출력
        log.info("생성된 JWT 토큰: {}", token);

        return token;
    }

    public String getNickname(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 문자열 이후의 토큰만 추출
        }

        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
