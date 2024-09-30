package com.fluffy.SharingCalendar.config;

import com.fluffy.SharingCalendar.filter.JwtAuthenticationFilter;
import com.fluffy.SharingCalendar.service.UserService;
import com.fluffy.SharingCalendar.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserService userService) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/login").permitAll()     // 로그인은 허용
                        .requestMatchers(HttpMethod.POST, "/api/users/validation").permitAll() // 닉네임 중복 검사 허용
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userService), UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOriginPattern("*"); // 모든 도메인 허용
        config.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.setAllowCredentials(true); // 자격 증명 허용

        source.registerCorsConfiguration("/**", config); // 모든 엔드포인트에 대해 CORS 설정 적용
        return source;
    }
}
