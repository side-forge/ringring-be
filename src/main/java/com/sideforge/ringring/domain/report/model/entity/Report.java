package com.sideforge.ringring.domain.report.model.entity;

import com.sideforge.ringring.domain.report.model.enums.ReportStatusType;
import com.sideforge.ringring.domain.report.model.enums.ReportType;
import com.sideforge.ringring.domain.notification.model.entity.ReportNotification;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    // 신고 상태 이력
    @OneToMany(mappedBy = "report", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<ReportStatusHistory> reportStatusHistories = new ArrayList<>();

    // 신고 알림 리스트
    @OneToMany(mappedBy = "report", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<ReportNotification> reportNotifications = new ArrayList<>();

    // 신고 투표 리스트
    @OneToMany(mappedBy = "report", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<ReportVote> reportVotes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.status = ReportStatusType.PENDING;
    }
}
