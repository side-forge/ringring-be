package com.sideforge.ringring.api.domain.auth.security.jwt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideforge.ringring.exception.dto.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class JwtTokenArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JwtTokenPayload.class) && parameter.getParameterType().equals(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = jwtTokenProvider.resolveToken(request);

        JwtTokenPayload jwtTokenPayload = parameter.getParameterAnnotation(JwtTokenPayload.class);
        boolean required = jwtTokenPayload == null || jwtTokenPayload.required();

        if (token == null || token.isBlank()) {
            if (required) throw new InvalidTokenException("Token is missing.");
            return null;
        }

        if (jwtTokenPayload != null) {
            String payload = decodeJwtPayloadUnsafe(token);
            String claimName = jwtTokenPayload.claim().getName();

            try {
                JsonNode payloadJson = objectMapper.readTree(payload);
                JsonNode claim = payloadJson.get(claimName);

                return claim != null ? claim.asText() : null;
            } catch (InvalidTokenException e) {
                throw e;
            } catch (Exception e) {
                throw new InvalidTokenException("Invalid token payload.", e);
            }
        }

        return null;
    }

    private String decodeJwtPayloadUnsafe(String token) {
        String[] parts = token.split("\\.");
        if (parts.length < 2) throw new InvalidTokenException("Invalid token format.");
        try {
            byte[] decoded = Base64.getUrlDecoder().decode(parts[1]);
            return new String(decoded, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException ex) {
            throw new InvalidTokenException("Invalid Base64URL in token payload.", ex);
        }
    }
}
