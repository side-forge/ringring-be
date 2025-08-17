package com.sideforge.ringring.domain.account.repository;

import com.sideforge.ringring.domain.account.model.entity.EmailVerificationCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationCodeRepository extends CrudRepository<EmailVerificationCode, String> {
}
