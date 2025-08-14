package com.sideforge.ringring.domain.post.model.entity;

import com.sideforge.ringring.domain.attachment.model.entity.Attachment;
import com.sideforge.ringring.domain.post.model.entity.id.PostAttachmentMappingId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_post_attachment_mapping")
public class PostAttachmentMapping {

    @EmbeddedId
    private PostAttachmentMappingId id;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @ToString.Exclude
    private Post post;

    @MapsId("attachmentId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachment_id")
    @ToString.Exclude
    private Attachment attachment;
}
