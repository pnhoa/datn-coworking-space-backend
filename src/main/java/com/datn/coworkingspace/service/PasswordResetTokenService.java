package com.datn.coworkingspace.service;

import com.datn.coworkingspace.entity.PasswordResetToken;
import com.datn.coworkingspace.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
public class PasswordResetTokenService implements IPasswordResetTokenService {


    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    @Override
    public boolean verifyExpiration(PasswordResetToken token) {
        if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
            passwordResetTokenRepository.delete(token);
            return false;
        }
        return true;
    }
}
