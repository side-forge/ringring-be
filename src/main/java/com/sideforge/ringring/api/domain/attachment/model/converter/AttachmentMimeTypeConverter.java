package com.sideforge.ringring.api.domain.attachment.model.converter;

import com.sideforge.ringring.exception.dto.InvalidRequestContentsException;
import com.sideforge.ringring.api.domain.attachment.model.enums.AttachmentMimeType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class AttachmentMimeTypeConverter implements AttributeConverter<AttachmentMimeType, String> {
    @Override
    public String convertToDatabaseColumn(AttachmentMimeType attribute) {
        return (attribute == null) ? null : attribute.getMimeType();
    }

    @Override
    public AttachmentMimeType convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        AttachmentMimeType type = AttachmentMimeType.fromMimeType(dbData);
        if (type == null) {
            throw new InvalidRequestContentsException("Unknown/unsupported MIME type: " + dbData);
        }
        return type;
    }
}
