package com.sideforge.ringring.api.domain.account.model.entity;

import com.sideforge.ringring.api.domain.account.model.enums.AccountStatusType;
import com.sideforge.ringring.api.domain.account.model.enums.SocialProviderType;
import com.sideforge.ringring.api.domain.post.model.entity.Post;
import com.sideforge.ringring.api.domain.report.model.entity.Report;
import com.sideforge.ringring.api.domain.report.model.entity.ReportNotification;
import com.sideforge.ringring.api.domain.report.model.entity.ReportVote;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "tb_account",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email")
        }
)
public class Account {
    @Id
    @Column(columnDefinition = "CHAR(32)", nullable = false)
    private String id;

    @Column(nullable = true, unique = true, length = 100)
    private String email;

    @Column(nullable = true, length = 255)
    private String password;

    @Column(nullable = false)
    private Boolean isSocial;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private SocialProviderType socialProvider;

    @Column(nullable = true, length = 255)
    private String providerId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatusType status;

    @Column(nullable = true)
    private Integer failedLoginCount;

    @Column(nullable = true)
    private LocalDateTime lastLoginAt;

    @Column(nullable = true)
    private LocalDateTime isLockedAt;

    // 사용자 권한 리스트
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @ToString.Exclude
    private List<AccountRoleMapping> accountRoles = new ArrayList<>();

    // 제재당한 이력
    @OneToMany(mappedBy = "bannedAccount", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<AccountBanHistory> banHistories = new ArrayList<>();

    // 제재를 준 이력 (관리자 입장)
    @OneToMany(mappedBy = "adminAccount", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private List<AccountBanHistory> banActions = new ArrayList<>();

    // 작성한 게시글 리스트
    @OneToMany(mappedBy = "authorAccount", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private List<Post> posts = new ArrayList<>();

    // 신고 목록
    @OneToMany(mappedBy = "reportedBy", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private List<Report> reports = new ArrayList<>();

    // 신고 게시글 알림 등록 리스트
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<ReportNotification> reportNotifications = new ArrayList<>();

    // 사용자 신고 게시물 투표 리스트
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private List<ReportVote> reportVotes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = AccountStatusType.ACTIVE;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void unlock() {
        this.status = AccountStatusType.ACTIVE;
        this.isLockedAt = null;
        this.failedLoginCount = 0;
    }

    public void unban() {
        this.status = AccountStatusType.ACTIVE;
    }

    public void resetFailedLoginCount() {
        this.failedLoginCount = 0;
    }

    public void updateLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public void increaseFailedLoginCount(int maxFailCount) {
        if (this.status == AccountStatusType.LOCKED) {
            return;
        }

        int count = (this.failedLoginCount == null ? 0 : this.failedLoginCount) + 1;
        this.failedLoginCount = count;

        if (count >= maxFailCount) {
            this.status = AccountStatusType.LOCKED;
            this.isLockedAt = LocalDateTime.now();
        }
    }
}
