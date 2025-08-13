package com.sideforge.ringring.domain.attachment.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// 첨부파일 마임타입 별칭
@Getter
public enum AttachmentMimeTypeAlias {
    JPG("image/jpg", AttachmentMimeType.JPEG, "JPEG 별칭(legacy IE/라이브러리)"),
    PJPEG("image/pjpeg", AttachmentMimeType.JPEG, "Progressive JPEG 별칭"),
    X_PNG("image/x-png", AttachmentMimeType.PNG, "PNG 별칭(구형 브라우저)"),

    AUDIO_X_WAV("audio/x-wav", null, "WAV 별칭"),
    AUDIO_MP3("audio/mp3", AttachmentMimeType.MP3, "MP3 별칭"),

    ZIP_COMPRESSED("application/x-zip-compressed", AttachmentMimeType.ZIP, "ZIP 별칭(Windows)"),
    X_PDF("application/x-pdf", AttachmentMimeType.PDF, "PDF 별칭"),
    X_HWP("application/x-hwp", AttachmentMimeType.HWP, "HWP 별칭");

    private static final Map<String, AttachmentMimeTypeAlias> ATTACHMENT_MIME_TYPE_ALIAS_MAP =
            Collections.unmodifiableMap(
                    Arrays.stream(values())
                            .collect(Collectors.toMap(
                                    a -> normalize(a.alias), // 정규화된 키
                                    Function.identity()
                            ))
            );

    private final String alias;
    private final AttachmentMimeType standardType;
    private final String description;

    AttachmentMimeTypeAlias(String alias, AttachmentMimeType standardType, String description) {
        this.alias = alias;
        this.standardType = standardType;
        this.description = description;
    }

    public static AttachmentMimeTypeAlias fromAlias(String alias) {
        return ATTACHMENT_MIME_TYPE_ALIAS_MAP.get(normalize(alias));
    }

    /**
     * 입력이 표준이든 별칭이든 표준 AttachmentMimeType으로 변환한다.
     * @param mimeType ex) "image/jpg" 또는 "image/jpeg"
     * @return 매칭되는 표준 타입, 없으면 null
     */
    public static AttachmentMimeType resolveToStandard(String mimeType) {
        String key = normalize(mimeType);
        // 1. 표준 직접 매칭
        AttachmentMimeType direct = AttachmentMimeType.fromMimeType(key);
        if (direct != null) return direct;

        // 2. 별칭 매칭
        AttachmentMimeTypeAlias alias = ATTACHMENT_MIME_TYPE_ALIAS_MAP.get(key);
        return (alias != null) ? alias.standardType : null;
    }

    public AttachmentMimeType toStandard() {
        return standardType;
    }

    private static String normalize(String s) {
        if (s == null) return null;
        // 세미콜론 이후 파라미터 제거, 공백 트림, 소문자화
        int semi = s.indexOf(';');
        String base = (semi >= 0 ? s.substring(0, semi) : s).trim();
        return base.toLowerCase(Locale.ROOT);
    }
}
