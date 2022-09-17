package com.datn.coworkingspace.service;

import com.datn.coworkingspace.entity.PasswordResetToken;

import java.util.Optional;

public interface IPasswordResetTokenService {

    Optional<PasswordResetToken> findByToken(String token);

    boolean verifyExpiration(PasswordResetToken token);

}
