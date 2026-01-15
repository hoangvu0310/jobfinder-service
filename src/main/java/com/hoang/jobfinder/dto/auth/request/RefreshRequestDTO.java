package com.hoang.jobfinder.dto.auth.request;

import com.hoang.jobfinder.common.Enum;
import lombok.Data;

@Data
public class RefreshRequestDTO {
  private String refreshToken;
  private Long userId;
  private String deviceId;
  private Enum.Platform platform;
}
