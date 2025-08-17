package com.sideforge.ringring.domain.report.model.entity.id;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class ReportAttachmentMappingId implements Serializable {
    private Long reportId;
    private String attachmentId;
}
