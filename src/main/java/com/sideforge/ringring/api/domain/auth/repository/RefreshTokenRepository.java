package com.sideforge.ringring.api.domain.auth.repository;

import com.sideforge.ringring.api.domain.auth.model.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
