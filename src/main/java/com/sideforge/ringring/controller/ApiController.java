package com.sideforge.ringring.controller;

import com.sideforge.ringring.common.util.CodeGenerator;
import com.sideforge.ringring.model.dto.res.ApiCommonResDto;
import com.sideforge.ringring.model.entity.Account;
import com.sideforge.ringring.model.entity.AccountRole;
import com.sideforge.ringring.model.entity.AccountRoleMapping;
import com.sideforge.ringring.model.entity.id.AccountRoleMappingId;
import com.sideforge.ringring.model.enums.AccountRoleType;
import com.sideforge.ringring.model.enums.ApiResponseCode;
import com.sideforge.ringring.repository.AccountRepository;
import com.sideforge.ringring.repository.AccountRoleMappingRepository;
import com.sideforge.ringring.repository.AccountRoleRepository;
import com.sideforge.ringring.security.principal.CustomUserPrincipal;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/dev")
@RequiredArgsConstructor
public class ApiController {

    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final AccountRoleMappingRepository accountRoleMappingRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    /** 테스트 계정 생성 */
    @Transactional
    @PostMapping("/users/signup")
    public ResponseEntity<ApiCommonResDto<?>> createAccountTest(
            @RequestBody CreateAccountDtoTest reqBodyDto
    ) {
        Account tAccount = Account.builder()
                .id(CodeGenerator.generateId("TA"))
                .email(reqBodyDto.getEmail())
                .password(passwordEncoder.encode(reqBodyDto.password))
                .isSocial(false)
                .name(reqBodyDto.getName())
                .nickname(reqBodyDto.getNickname())
                .phoneNumber(reqBodyDto.getPhoneNumber())
                .build();

        tAccount = accountRepository.save(tAccount);
        AccountRole tAccountRole = accountRoleRepository.findById(AccountRoleType.ROLE_USER.getRoleId()).get();

        accountRoleMappingRepository.save(AccountRoleMapping.of(tAccount, tAccountRole));

        return ResponseEntity
                .status(ApiResponseCode.SUCCESS.getHttpStatus())
                .body(ApiCommonResDto.<Void>builder()
                        .code(ApiResponseCode.SUCCESS.getCode())
                        .message(ApiResponseCode.SUCCESS.formatMessage())
                        .build()
                );
    }

    @Data
    public static class CreateAccountDtoTest {
        private String email;
        private String password;
        private String name;
        private String nickname;
        private String phoneNumber;
    }
}
