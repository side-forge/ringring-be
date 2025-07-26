package com.sideforge.ringring.model.dto.req.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationReqBodyDto {
    @NotBlank(message = "Email must not be blank.")
    @Email(message = "Invalid email format.")
    private String email;
}
