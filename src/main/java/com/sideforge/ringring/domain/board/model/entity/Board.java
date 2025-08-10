package com.sideforge.ringring.domain.board.model.entity;

import com.sideforge.ringring.domain.account.model.converter.AccountRoleTypeConverter;
import com.sideforge.ringring.domain.account.model.enums.AccountRoleType;
import com.sideforge.ringring.domain.board.model.enums.BoardType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BoardType type;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "read_min_role", nullable = false, columnDefinition = "CHAR(5)")
    @Convert(converter = AccountRoleTypeConverter.class)
    private AccountRoleType readMinRole;

    @Column(name = "write_min_role", nullable = false, columnDefinition = "CHAR(5)")
    @Convert(converter = AccountRoleTypeConverter.class)
    private AccountRoleType writeMinRole;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
