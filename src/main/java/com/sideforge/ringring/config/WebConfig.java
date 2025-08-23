package com.sideforge.ringring.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideforge.ringring.api.domain.auth.security.jwt.JwtTokenArgumentResolver;
import com.sideforge.ringring.api.domain.auth.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new JwtTokenArgumentResolver(jwtTokenProvider, objectMapper));
    }
}
