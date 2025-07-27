package com.sideforge.ringring.service;

import com.sideforge.ringring.common.exception.dto.AccountNotFoundException;
import com.sideforge.ringring.model.entity.Account;
import com.sideforge.ringring.model.enums.ApiResponseCode;
import com.sideforge.ringring.repository.AccountRepository;
import com.sideforge.ringring.security.principal.CustomUserPrincipal;
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
