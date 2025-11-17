package com.splitnice.authservice.service;

import com.splitnice.authservice.entities.RefreshToken;
import com.splitnice.authservice.entities.UserInfo;
import com.splitnice.authservice.repository.RefreshTokenRepository;
import com.splitnice.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

  @Autowired RefreshTokenRepository refreshTokenRepository;
  @Autowired UserRepository userRepository;

  public RefreshToken createRefreshToken(String username) {
    UserInfo extractuserinfo = userRepository.findByUsername(username);
    RefreshToken refreshToken =
        RefreshToken.builder()
            .userInfo(extractuserinfo)
            .token(UUID.randomUUID().toString())
            .expiryDate(Instant.now().plusMillis(60000))
            .build();

    return refreshTokenRepository.save(refreshToken);
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().isBefore(Instant.now())) {
      // Token expired → delete and throw exception
      refreshTokenRepository.delete(token);
      throw new RuntimeException("Refresh token was expired. Please sign in again.");
    }
    return token;
  }

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }
}
