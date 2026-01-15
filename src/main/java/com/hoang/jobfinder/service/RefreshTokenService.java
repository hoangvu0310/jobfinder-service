package com.hoang.jobfinder.service;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.dto.auth.request.RefreshRequestDTO;
import com.hoang.jobfinder.entity.User;

public interface RefreshTokenService {
  String createRefreshToken(User user, String deviceId, Enum.Platform platform);
  String refresh(RefreshRequestDTO refreshRequestDTO);
  void deleteToken(Long userId);
}
