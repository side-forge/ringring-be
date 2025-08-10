package com.sideforge.ringring.domain.report.model.entity;

import com.sideforge.ringring.domain.post.model.entity.Post;
import com.sideforge.ringring.domain.report.model.entity.id.ReportPostMappingId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_report_post_mapping")
public class ReportPostMapping {
    @Id
    private ReportPostMappingId id;

    @MapsId("reportId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    @ToString.Exclude
    private Report report;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @ToString.Exclude
    private Post post;
}
