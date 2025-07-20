package com.sideforge.ringring.model.entity;

import com.sideforge.ringring.model.enums.AccountBanType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_account_ban_history")
public class AccountBanHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banned_id", nullable = false)
    @ToString.Exclude
    private Account bannedAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = true)
    @ToString.Exclude
    private Account adminAccount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountBanType type;

    @Column(nullable = false, length = 255)
    private String reason;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column(nullable = true)
    private LocalDateTime endsAt;
}
