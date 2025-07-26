package com.sideforge.ringring.repository;

import com.sideforge.ringring.model.entity.redis.EmailVerificationCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationCodeRepository extends CrudRepository<EmailVerificationCode, String> {
}
