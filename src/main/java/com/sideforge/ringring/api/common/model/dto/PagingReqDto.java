package com.sideforge.ringring.api.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagingReqDto {
    private Integer pageSize;
    private Integer pageNumber;

    public void initialize() {
        this.pageSize = (this.pageSize == null || this.pageSize <= 0) ? 10 : this.pageSize;
        this.pageNumber = (this.pageNumber == null || this.pageNumber <= 0) ? 1 : this.pageNumber;
    }
}
