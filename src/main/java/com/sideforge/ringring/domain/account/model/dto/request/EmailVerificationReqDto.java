package com.sideforge.ringring.domain.account.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationReqDto {
    @NotBlank(message = "Email must not be blank.")
    @Email(message = "Invalid email format.")
    private String email;
}
