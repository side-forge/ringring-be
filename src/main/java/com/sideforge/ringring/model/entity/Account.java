package com.sideforge.ringring.model.entity;

import com.sideforge.ringring.model.enums.AccountStatusType;
import com.sideforge.ringring.model.enums.SocialProviderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
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
    private Long id;

    @Column(nullable = true, unique = true, length = 100)
    private String email;

    @Column(nullable = true, length = 255)
    private String password;

    @Column(nullable = false)
    private boolean isSocial;

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

    @Column(nullable = false, updatable = false)
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

    // 제재당한 이력
    @OneToMany(mappedBy = "bannedAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountBanHistory> banHistories = new ArrayList<>();

    // 제재를 준 이력 (관리자 입장)
    @OneToMany(mappedBy = "adminAccount", cascade = CascadeType.PERSIST)
    private List<AccountBanHistory> banActions = new ArrayList<>();

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
}
