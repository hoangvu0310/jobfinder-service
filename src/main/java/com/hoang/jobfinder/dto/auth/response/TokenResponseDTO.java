package com.hoang.jobfinder.dto.auth.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponseDTO {
  private String accessToken;

  private String refreshToken;

  private AccountInfoDTO user;
}
