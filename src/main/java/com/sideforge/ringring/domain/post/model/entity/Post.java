package com.sideforge.ringring.domain.post.model.entity;

import com.sideforge.ringring.domain.account.model.entity.Account;
import com.sideforge.ringring.domain.account.model.entity.AccountRoleMapping;
import com.sideforge.ringring.domain.post.model.enums.PostType;
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
@Table(name = "tb_post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = true)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = true)
    private Account authorAccount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostType type;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false)
    private Integer viewCount;

    // 현재 게시글의 컨텐츠 번호
    @Builder.Default
    @Column(name = "content_version", nullable = false)
    private int contentVersion = 0;

    // 게시글 내 컨텐츠 목록
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private List<PostContent> contents = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<PostAttachmentMapping> postAttachmentMappings;

    /** 최초 컨텐츠 생성 (생성 트랜잭션에서 반드시 호출) */
    public PostContent initializeFirstVersion(String contentText) {
        if (this.contentVersion != 0) {
            throw new IllegalStateException("Already initialized. contentVersion=" + contentVersion);
        }
        PostContent v1 = PostContent.builder()
                .post(this)
                .versionNo(1)
                .content(contentText)
                .build();
        this.contents.add(v1);
        this.contentVersion = 1;
        touch();
        return v1;
    }

    /** 새 버전 컨텐츠 추가 */
    public PostContent createNewVersion(String contentText) {
        int next = this.contentVersion + 1;
        PostContent nv = PostContent.builder()
                .post(this)
                .versionNo(next)
                .content(contentText)
                .build();
        this.contents.add(nv);
        this.contentVersion = next;
        touch();
        return nv;
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        this.viewCount = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isActive = true;

        // 저장 직전에 반드시 버전은 1
        if (this.contentVersion == 1) {
            throw new IllegalStateException("Post must be initialized with v1 before persist.");
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
