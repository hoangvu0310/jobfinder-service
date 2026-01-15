package com.hoang.jobfinder.service.impl;

import com.hoang.jobfinder.common.ResultCode;
import com.hoang.jobfinder.dto.auth.request.RefreshRequestDTO;
import com.hoang.jobfinder.entity.RefreshToken;
import com.hoang.jobfinder.entity.User;
import com.hoang.jobfinder.exception.JobFinderException;
import com.hoang.jobfinder.property.AuthTokenProperties;
import com.hoang.jobfinder.repository.RefreshTokenRepository;
import com.hoang.jobfinder.repository.UserRepository;
import com.hoang.jobfinder.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

  private AuthTokenProperties authTokenProperties;

  private RefreshTokenRepository refreshTokenRepository;

  private UserRepository userRepository;

  @Override
  @Transactional(rollbackOn = Exception.class)
  public String createRefreshToken(@NonNull User user) {
    Optional<RefreshToken> optionalCurrentToken = refreshTokenRepository.findRefreshTokenByUserId(user.getUserId());

    String token = UUID.randomUUID().toString();
    Instant expireDate = Instant.now().plusSeconds(authTokenProperties.getRefreshTokenTTL() * 60);

    if (optionalCurrentToken.isPresent()) {
      RefreshToken currentToken = optionalCurrentToken.get();
      currentToken.setToken(token);
      currentToken.setExpirationDate(expireDate);
      currentToken.setUpdatedBy(user.getUsername());
    } else {
      refreshTokenRepository.save(
          RefreshToken.builder()
          .token(token)
          .expirationDate(expireDate)
          .user(user)
          .createdBy(user.getUsername())
          .build()
      );
    }

    return token;
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public String refresh(RefreshRequestDTO refreshRequestDTO) throws JobFinderException {
    Boolean isTokenValid = validateRefreshToken(refreshRequestDTO.getRefreshToken(), refreshRequestDTO.getUserId());

    if (isTokenValid) {
      User user = userRepository.findUserByUserId(refreshRequestDTO.getUserId());
      return createRefreshToken(user);
    } else {
      throw new JobFinderException(ResultCode.UNAUTHORIZED);
    }
  }

  @Override
  @Transactional
  public void deleteToken(Long userId) {
    refreshTokenRepository.deleteRefreshTokenByUserId(userId);
  }


  private Boolean validateRefreshToken(String token, Long userId) {
    Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findRefreshTokenByUserId(userId);

    return optionalRefreshToken
        .map(refreshToken ->
            refreshToken.getExpirationDate().isAfter(Instant.now()) && refreshToken.getToken().equals(token)
        )
        .orElse(false);

  }
}
