package org.example.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.example.entities.RefreshToken;
import org.example.entities.UserInfo;
import org.example.repository.RefreshTokenRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    return refreshTokenRepository.findByRefreshToken(token);
  }
}
