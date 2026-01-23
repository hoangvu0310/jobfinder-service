package com.hoang.jobfinder.service;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.dto.auth.request.RefreshRequestDTO;
import com.hoang.jobfinder.entity.base.AccountBaseEntity;

public interface RefreshTokenService {
  String createRefreshToken(AccountBaseEntity user, String deviceId, Enum.Platform platform, Boolean isHR);
  String refresh(RefreshRequestDTO refreshRequestDTO, Boolean isHR);
  void deleteToken(Long userId, Boolean isHR);
}
