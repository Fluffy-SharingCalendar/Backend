package com.fluffy.SharingCalendar.util;

import com.fluffy.SharingCalendar.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
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

    // 비밀 키를 SecretKeySpec을 사용하여 Key 객체로 생성
    private Key getSigningKey() {
        return new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(User user) {
        String token = Jwts.builder()
                .setSubject(user.getLoginId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY_SECONDS * 1000))
                .signWith(getSigningKey())
                .compact();

        // 생성된 토큰을 로그로 출력
        log.info("생성된 JWT 토큰: {}", token);

        return token;
    }

    public String getLoginId(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 문자열 이후의 토큰만 추출
        }

        Claims claims = Jwts.parserBuilder() // parser() -> parserBuilder()로 변경
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder() // parser() -> parserBuilder()로 변경
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
