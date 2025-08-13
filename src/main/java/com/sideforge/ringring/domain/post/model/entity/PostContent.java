package com.sideforge.ringring.domain.post.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "tb_post_content",
        uniqueConstraints = {
                @UniqueConstraint(name = "ux_post_id_version_no", columnNames = {"post_id","version_no"})
        }
)
public class PostContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    @ToString.Exclude
    private Post post;

    @Column(name = "version_no", nullable = false, updatable = false)
    private int versionNo;

    @Column(nullable = false, columnDefinition = "TEXT", updatable = false)
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
