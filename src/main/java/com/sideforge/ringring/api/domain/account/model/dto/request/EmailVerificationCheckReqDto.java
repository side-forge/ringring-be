package com.sideforge.ringring.api.domain.account.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationCheckReqDto {
    @NotBlank(message = "Email must not be blank.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\d{6}$", message = "Invalid code format.")
    private String code;
}
