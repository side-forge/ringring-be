package com.sideforge.ringring.model.entity.id;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class AccountRoleMappingId implements Serializable {
    private String accountId;
    private String roleId;
}
