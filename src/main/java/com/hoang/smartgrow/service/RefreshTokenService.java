package com.hoang.smartgrow.service;

import com.hoang.smartgrow.dto.auth.request.RefreshRequestDTO;
import com.hoang.smartgrow.entity.User;

public interface RefreshTokenService {
  String createRefreshToken(User user);
  String refresh(RefreshRequestDTO refreshRequestDTO);
}
