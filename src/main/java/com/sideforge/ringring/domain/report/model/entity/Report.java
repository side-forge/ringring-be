package com.sideforge.ringring.domain.report.model.entity;

import com.sideforge.ringring.domain.account.model.entity.Account;
import com.sideforge.ringring.domain.report.model.enums.ReportReviewViaType;
import com.sideforge.ringring.domain.report.model.enums.ReportStatusType;
import com.sideforge.ringring.domain.report.model.enums.ReportType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "tb_report",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_phone_number_type", columnNames = {"phoneNumber", "type"})
        },
        indexes = {
                @Index(name = "idx_phone_number_type", columnList = "phoneNumber, type")
        }
)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportStatusType status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by", nullable = false)
    @ToString.Exclude
    private Account reportedBy;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportReviewViaType reviewVia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by", nullable = true)
    @ToString.Exclude
    private Account reviewedBy;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal weight;

    @Column(nullable = false)
    private Integer viewCount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.status = ReportStatusType.PENDING;
        this.viewCount = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.weight = BigDecimal.ZERO;
        this.reviewVia = ReportReviewViaType.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
