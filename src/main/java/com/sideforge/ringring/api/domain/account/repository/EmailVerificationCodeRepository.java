package com.sideforge.ringring.api.domain.account.repository;

import com.sideforge.ringring.api.domain.account.model.entity.EmailVerificationCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationCodeRepository extends CrudRepository<EmailVerificationCode, String> {
}
