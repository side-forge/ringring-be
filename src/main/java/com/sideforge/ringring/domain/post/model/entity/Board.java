package com.sideforge.ringring.domain.post.model.entity;

import com.sideforge.ringring.domain.account.model.converter.AccountRoleTypeConverter;
import com.sideforge.ringring.domain.account.model.enums.AccountRoleType;
import com.sideforge.ringring.domain.post.model.enums.BoardType;
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

    // 게시판 내 게시글 리스트
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private List<Post> posts = new ArrayList<>();

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
