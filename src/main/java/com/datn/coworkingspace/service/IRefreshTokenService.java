package com.datn.coworkingspace.service;

import com.datn.coworkingspace.entity.RefreshToken;

import java.util.Optional;

public interface IRefreshTokenService {

    Optional<RefreshToken> findByToken(String token);

    RefreshToken createRefreshToken(Long userId);

    RefreshToken verifyExpiration(RefreshToken token);

    int deleteByUserId(Long userId);
}
