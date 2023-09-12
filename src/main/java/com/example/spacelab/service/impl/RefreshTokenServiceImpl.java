package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.model.RefreshToken;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.RefreshTokenRepository;
import com.example.spacelab.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Log
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final AdminRepository adminRepository;

    @Override
    public RefreshToken findByToken(String token) {
        return repository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("Refresh token not found!"));
    }

    @Override
    public RefreshToken createRefreshToken(String username) {
        Admin admin = adminRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("Admin not found!"));
        Optional<RefreshToken> opt = repository.findByAdmin(admin);
        RefreshToken token = opt.orElseGet(RefreshToken::new);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plus(Duration.ofDays(7)));
        token.setAdmin(admin);
        return repository.save(token);
    }
}
