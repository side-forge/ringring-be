package com.sideforge.ringring.api.domain.auth.model.principal;

import com.sideforge.ringring.api.domain.account.model.entity.Account;
import com.sideforge.ringring.api.domain.account.model.entity.AccountRole;
import com.sideforge.ringring.api.domain.account.model.entity.AccountRoleMapping;
import com.sideforge.ringring.api.domain.account.model.enums.AccountRoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserPrincipal implements UserDetails {
    private final String id;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    private CustomUserPrincipal(
            String id,
            String email,
            String password,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    // DB 조회 Account 기반
    public static CustomUserPrincipal from(Account account) {
        List<SimpleGrantedAuthority> authorities = account.getAccountRoles().stream()
                .map(AccountRoleMapping::getRole)
                .map(AccountRole::getId)
                .map(AccountRoleType::fromRoleId)
                .map(AccountRoleType::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new CustomUserPrincipal(
                account.getId(),
                account.getEmail(),
                account.getPassword(),
                authorities
        );
    }

    // jwt token 정보 기반
    public static CustomUserPrincipal from(String id, String email, List<SimpleGrantedAuthority> authorities) {
        return new CustomUserPrincipal(
                id,
                email,
                "",
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getAccountId() {
        return this.id;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        // username == email
        return this.email;
    }
}
