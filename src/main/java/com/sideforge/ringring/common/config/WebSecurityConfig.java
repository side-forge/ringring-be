package com.sideforge.ringring.common.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final String[] EXCLUDE_PATHS = {
            "/**",
            "/{url}"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(@NonNull HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        // 허용할 도메인 리스트
                        configuration.setAllowedOriginPatterns(Arrays.asList(
                                "http://localhost:9500",
                                "http://localhost:9000",
                                "http://localhost:8000",
                                "capacitor://*"
                        ));
                        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setMaxAge(3600L);
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));

        // csrf disable
        http
                .csrf(AbstractHttpConfigurer::disable);
        // 경로별 인가 설정
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(EXCLUDE_PATHS).permitAll() // EXCLUDE_PATHS에 포함된 경로는 인증 없이 접근 가능
                        .anyRequest().authenticated());             // 그 외 모든 요청은 인증 필요
        // Form 로그인 방식 사용
        http
                .formLogin(AbstractHttpConfigurer::disable);
        // 세션 설정, 동시 로그인 세션 1개로 제한하고 기존 세션이 만료되면 '/login?expired=true'로 리다이렉트
        // ToDo. 리다이렉트 URL은 예시일뿐이며 기능 구현 시 변경이 필요
        http
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .maximumSessions(1)
                        .expiredUrl("/login?expired=ture"));

        return http.build();
    }
}
