package com.sideforge.ringring.api.common.controller;

import com.sideforge.ringring.api.common.model.dto.ApiCommonResDto;
import com.sideforge.ringring.api.common.model.enums.ApiResponseCode;
import com.sideforge.ringring.api.common.service.ResponseService;
import com.sideforge.ringring.util.CodeGenerator;
import com.sideforge.ringring.api.domain.account.model.entity.Account;
import com.sideforge.ringring.api.domain.account.model.entity.AccountRole;
import com.sideforge.ringring.api.domain.account.model.entity.AccountRoleMapping;
import com.sideforge.ringring.api.domain.account.model.enums.AccountRoleType;
import com.sideforge.ringring.api.domain.account.repository.AccountRepository;
import com.sideforge.ringring.api.domain.account.repository.AccountRoleMappingRepository;
import com.sideforge.ringring.api.domain.account.repository.AccountRoleRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dev")
@RequiredArgsConstructor
public class ApiController {

    private final ResponseService responseService;
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
    public ResponseEntity<ApiCommonResDto<Void>> createAccountTest(
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

        return responseService.resSuccess();
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
