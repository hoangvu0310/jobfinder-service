package com.hoang.smartgrow.config.security;

import com.hoang.smartgrow.common.ResultCode;
import com.hoang.smartgrow.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private ObjectMapper objectMapper;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException
  ) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getOutputStream().write(objectMapper.writeValueAsBytes(ApiResponse.errorResponse(ResultCode.UNAUTHORIZED)));
  }
}
