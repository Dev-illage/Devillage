package com.devillage.teamproject.repository.token;

import com.devillage.teamproject.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findRefreshTokenByTokenValue(String token);
}
