package com.sideforge.ringring.api.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagingResDto {
    private Integer total;
    private Integer pageSize;
    private Integer pageNumber;
}
