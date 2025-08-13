package com.sideforge.ringring.domain.attachment.model.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 첨부파일 마임타입
@Getter
public enum AttachmentMimeType {
    JPEG("image/jpeg", "이미지(JPEG)"),
    PNG("image/png", "이미지(PNG)"),
    WEBP("image/webp", "이미지(WEBP)"),

    MP4("video/mp4", "동영상(MP4)"),
    WEBM("video/webm", "동영상(WEBM)"),

    MP3("audio/mpeg", "오디오(MP3)"),
    M4A("audio/mp4", "오디오(M4A)"),

    PDF("application/pdf", "문서(PDF)"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "문서(DOCX)"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "문서(XLSX)"),
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation", "문서(PPTX)"),
    HWP("application/vnd.hancom.hwp", "문서(HWP)"),
    HWPX("application/vnd.hancom.hwpx", "문서(HWPX)"),
    TXT("text/plain", "문서(TXT)"),

    ZIP("application/zip", "압축(ZIP)");

    private static final Map<String, AttachmentMimeType> ATTACHMENT_MIME_TYPE_MAP =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(AttachmentMimeType::getMimeType, Function.identity())));

    private final String mimeType;
    private final String description;

    AttachmentMimeType(String mimeType, String description) {
        this.mimeType = mimeType;
        this.description = description;
    }

    public static AttachmentMimeType fromMimeType(String mimeType) {
        return ATTACHMENT_MIME_TYPE_MAP.get(mimeType);
    }
}
