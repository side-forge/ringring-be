package com.sideforge.ringring.api.domain.account.model.converter;

import com.sideforge.ringring.api.domain.account.model.enums.AccountRoleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class AccountRoleTypeConverter implements AttributeConverter<AccountRoleType, String> {
    @Override
    public String convertToDatabaseColumn(AccountRoleType attribute) {
        return attribute == null ? null : attribute.getRoleId(); // "R0000" 등
    }
    @Override
    public AccountRoleType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : AccountRoleType.fromRoleId(dbData);
    }
}
