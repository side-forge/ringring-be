package com.sideforge.ringring.domain.attachment.model.entity;

import com.sideforge.ringring.domain.attachment.model.converter.AttachmentMimeTypeConverter;
import com.sideforge.ringring.domain.attachment.model.enums.AttachmentMimeType;
import com.sideforge.ringring.domain.attachment.model.enums.AttachmentType;
import com.sideforge.ringring.domain.post.model.entity.PostAttachmentMapping;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_attachment")
public class Attachment {
    @Id
    @Column(columnDefinition = "CHAR(32)", nullable = false)
    private String id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AttachmentType type;

    @Convert(converter = AttachmentMimeTypeConverter.class)
    @Column(name = "mime_type", nullable = false, length = 100)
    private AttachmentMimeType mimeType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String url;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "attachment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<PostAttachmentMapping> postAttachmentMappings;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
