package com.sideforge.ringring.api.domain.account.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailDupCheckResDto {
    private boolean isDuplicate;
}
