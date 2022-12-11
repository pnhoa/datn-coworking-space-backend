package com.datn.coworkingspace.repository;

import com.datn.coworkingspace.entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {

    Optional<Blacklist> findByToken(String token);
}
