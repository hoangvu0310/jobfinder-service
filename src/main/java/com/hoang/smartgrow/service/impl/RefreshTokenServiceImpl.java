package com.hoang.smartgrow.service.impl;

import com.hoang.smartgrow.common.ResultCode;
import com.hoang.smartgrow.dto.auth.request.RefreshRequestDTO;
import com.hoang.smartgrow.entity.RefreshToken;
import com.hoang.smartgrow.entity.User;
import com.hoang.smartgrow.exception.SmartGrowException;
import com.hoang.smartgrow.property.AuthTokenProperties;
import com.hoang.smartgrow.repository.RefreshTokenRepository;
import com.hoang.smartgrow.repository.UserRepository;
import com.hoang.smartgrow.service.RefreshTokenService;
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
  @Transactional
  public String refresh(RefreshRequestDTO refreshRequestDTO) throws SmartGrowException {
    Boolean isTokenValid = validateRefreshToken(refreshRequestDTO.getRefreshToken(), refreshRequestDTO.getUserId());

    if (isTokenValid) {
      User user = userRepository.findUserByUserId(refreshRequestDTO.getUserId());
      return createRefreshToken(user);
    } else {
      throw new SmartGrowException(ResultCode.UNAUTHORIZED);
    }
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
