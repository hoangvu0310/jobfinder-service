package com.hoang.smartgrow.dto.auth.request;

import lombok.Data;

@Data
public class RefreshRequestDTO {
  private String refreshToken;
  private Long userId;
}
