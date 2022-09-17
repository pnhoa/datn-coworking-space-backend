package com.datn.coworkingspace.service;

import com.datn.coworkingspace.dto.LogoutRequest;
import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.entity.Blacklist;
import com.datn.coworkingspace.entity.RefreshToken;
import com.datn.coworkingspace.repository.BlacklistRepository;
import com.datn.coworkingspace.repository.RefreshTokenRepository;
import com.datn.coworkingspace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class BlacklistService implements IBlacklistService {

    @Autowired
    private BlacklistRepository blacklistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public Optional<Blacklist> findByToken(String token) {
        return blacklistRepository.findByToken(token);
    }

    @Override
    public MessageResponse addTokenToBlacklist(LogoutRequest logoutRequest) {
        if(!blacklistRepository.findByToken(logoutRequest.getToken()).isPresent() ){
            Blacklist blacklist = new Blacklist();
            blacklist.setUser(userRepository.findById(logoutRequest.getUserId()).get());
            blacklist.setToken(logoutRequest.getToken());

            blacklistRepository.save(blacklist);

            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(logoutRequest.getRefreshToken());

            if(refreshToken.isPresent()) {
                refreshTokenRepository.delete(refreshToken.get());
            } else {
                return new MessageResponse("Refresh token is incorrect!", HttpStatus.OK, LocalDateTime.now());
            }

            return new MessageResponse("Logout successfully!", HttpStatus.OK, LocalDateTime.now());
        }
        return new MessageResponse("Exist token in database", HttpStatus.OK, LocalDateTime.now());
    }
}
