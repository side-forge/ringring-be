package com.sideforge.ringring.api.domain.auth.service;

import com.sideforge.ringring.exception.dto.AccountNotFoundException;
import com.sideforge.ringring.api.domain.account.model.entity.Account;
import com.sideforge.ringring.api.common.model.enums.ApiResponseCode;
import com.sideforge.ringring.api.domain.account.repository.AccountRepository;
import com.sideforge.ringring.api.domain.auth.model.principal.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new AccountNotFoundException(ApiResponseCode.ACCOUNT_NOT_FOUND.getMessage()));

        return CustomUserPrincipal.from(account);
    }
}
