package com.sideforge.ringring.domain.report.model.entity;

import com.sideforge.ringring.domain.attachment.model.entity.Attachment;
import com.sideforge.ringring.domain.report.model.entity.id.ReportAttachmentMappingId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_report_attachment_mapping")
public class ReportAttachmentMapping {

    @EmbeddedId
    private ReportAttachmentMappingId id;

    @MapsId("reportId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    @ToString.Exclude
    private Report report;

    @MapsId("attachmentId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachment_id")
    @ToString.Exclude
    private Attachment attachment;
}
