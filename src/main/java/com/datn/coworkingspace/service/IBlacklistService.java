package com.datn.coworkingspace.service;

import com.datn.coworkingspace.dto.LogoutRequest;
import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.entity.Blacklist;

import java.util.Optional;

public interface IBlacklistService {

    Optional<Blacklist> findByToken(String token);

    MessageResponse addTokenToBlacklist(LogoutRequest logoutRequest);
}
