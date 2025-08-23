package com.sideforge.ringring.api.domain.auth.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenReissueReqDto {
    @NotBlank(message = "Token is required.")
    private String refreshToken;
}
