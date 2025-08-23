package com.sideforge.ringring.api.domain.report.model.entity;

import com.sideforge.ringring.api.domain.report.model.enums.ReportStatusType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_report_history")
public class ReportStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    @ToString.Exclude
    private Report report;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private ReportStatusType fromStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportStatusType toStatus;

    @Column(nullable = true)
    private String reason;

    @Column(nullable = true, columnDefinition = "CHAR(32)")
    private String createdBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
