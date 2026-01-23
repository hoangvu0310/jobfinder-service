package com.hoang.jobfinder.service.impl;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.common.ResultCode;
import com.hoang.jobfinder.dto.auth.request.RefreshRequestDTO;
import com.hoang.jobfinder.entity.HR;
import com.hoang.jobfinder.entity.RefreshToken;
import com.hoang.jobfinder.entity.base.AccountBaseEntity;
import com.hoang.jobfinder.entity.user.User;
import com.hoang.jobfinder.exception.JobFinderException;
import com.hoang.jobfinder.property.AuthTokenProperties;
import com.hoang.jobfinder.repository.HRRepository;
import com.hoang.jobfinder.repository.RefreshTokenRepository;
import com.hoang.jobfinder.repository.UserRepository;
import com.hoang.jobfinder.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

  private AuthTokenProperties authTokenProperties;

  private RefreshTokenRepository refreshTokenRepository;

  private UserRepository userRepository;

  private HRRepository hrRepository;

  @Override
  @Transactional(rollbackOn = Exception.class)
  public String createRefreshToken(@NonNull AccountBaseEntity user, String deviceId, Enum.Platform platform, Boolean isHR) {
    List<RefreshToken> refreshTokens = refreshTokenRepository.findRefreshTokenByUserId(user.getId(), isHR);

    String token = UUID.randomUUID().toString();
    Instant expireDate = Instant.now().plusSeconds(authTokenProperties.getRefreshTokenTTL() * 60);

    if (
        refreshTokens
          .stream()
          .anyMatch(refreshToken -> refreshToken.getDeviceId().equals(deviceId))
    ) {
      RefreshToken currentToken = refreshTokens
          .stream()
          .filter(refreshToken -> refreshToken.getDeviceId().equals(deviceId))
          .findAny()
          .orElse(null);

      if (currentToken != null) {
        currentToken.setToken(token);
        currentToken.setExpirationDate(expireDate);
        currentToken.setUpdatedBy(user.getEmail());
      }
    } else {
      refreshTokenRepository.save(
          RefreshToken.builder()
              .token(token)
              .expirationDate(expireDate)
              .user(isHR ? null : (User) user)
              .hr(isHR ? (HR) user : null)
              .createdBy(user.getEmail())
              .deviceId(deviceId)
              .platform(platform)
              .build()
      );
    }

    return token;
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public String refresh(RefreshRequestDTO refreshRequestDTO, Boolean isHR) throws JobFinderException {
    Boolean isTokenValid = validateRefreshToken(
        refreshRequestDTO.getRefreshToken(),
        refreshRequestDTO.getUserId(),
        refreshRequestDTO.getDeviceId(),
        isHR
    );

    if (isTokenValid) {
      if (isHR) {
        HR hr = hrRepository.findHRById(refreshRequestDTO.getUserId());
        return createRefreshToken(hr, refreshRequestDTO.getDeviceId(), refreshRequestDTO.getPlatform(), true);
      }
      User user = userRepository.findUserById(refreshRequestDTO.getUserId());
      return createRefreshToken(user, refreshRequestDTO.getDeviceId(), refreshRequestDTO.getPlatform(), false);

    } else {
      throw new JobFinderException(ResultCode.UNAUTHORIZED);
    }
  }

  @Override
  @Transactional
  public void deleteToken(Long userId, Boolean isHR) {
    refreshTokenRepository.deleteRefreshTokenByUserId(userId, isHR);
  }


  private Boolean validateRefreshToken(String token, Long userId, String deviceId, Boolean isHR) {
    List<RefreshToken> refreshTokens = refreshTokenRepository.findRefreshTokenByUserId(userId, isHR);

    return refreshTokens
        .stream()
        .map(refreshToken ->
            refreshToken.getExpirationDate().isAfter(Instant.now())
                && refreshToken.getToken().equals(token)
                && refreshToken.getDeviceId().equals(deviceId)
        )
        .findAny()
        .orElse(false);

  }
}
