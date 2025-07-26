package com.sideforge.ringring.model.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiCommonResDto<T> {
    private T data;
    private String code;
    private String message;
    private PagingResDto pagingData;
}
