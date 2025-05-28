package com.example.oo_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ CORS 허용
                .csrf(csrf -> csrf.disable())  // POST 요청 위해 임시로 꺼둠 (추후 토큰 방식 사용할 경우 필요 없음)
                .formLogin(form -> form.disable()) //  기본 로그인 폼 비활성화
                .httpBasic(basic -> basic.disable()) //  기본 인증 헤더 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/**", "/api/chatrooms", "/api/chatrooms/**", "/api/purchase/direct", "/api/books/**", "/api/schedule", "/api/mypage", "/api/recommendation/**", "/api/main", "/api/search/**").permitAll()
                        .anyRequest().authenticated()  // 그 외는 인증 필요
                );

        return http.build();
    }

    // ✅ CORS 설정 메서드 추가
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("*")); // Android 앱이 요청할 수 있게 전체 허용 (실서비스 시 특정 도메인/IP만)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // 필요에 따라 false로 설정 가능

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 적용

        return source;
    }
}
