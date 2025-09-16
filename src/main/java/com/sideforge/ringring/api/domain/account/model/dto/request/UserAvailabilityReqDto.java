package com.sideforge.ringring.api.domain.account.model.dto.request;

import com.sideforge.ringring.api.domain.account.model.enums.UserIdentifierType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAvailabilityReqDto {
    @NotNull
    private UserIdentifierType type;

    @NotBlank
    private String value;
}
