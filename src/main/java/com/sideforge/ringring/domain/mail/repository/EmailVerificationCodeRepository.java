package com.sideforge.ringring.domain.mail.repository;

import com.sideforge.ringring.domain.mail.model.entity.EmailVerificationCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationCodeRepository extends CrudRepository<EmailVerificationCode, String> {
}
