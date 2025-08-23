package com.sideforge.ringring.api.domain.auth.repository;

import com.sideforge.ringring.api.domain.auth.model.entity.BlacklistedAccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistedAccessTokenRepository extends CrudRepository<BlacklistedAccessToken, String> {
}
