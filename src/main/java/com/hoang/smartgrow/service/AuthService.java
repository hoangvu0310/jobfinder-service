package com.hoang.smartgrow.service;

import com.hoang.smartgrow.dto.auth.request.RefreshRequestDTO;
import com.hoang.smartgrow.dto.auth.request.SignInRequestDTO;
import com.hoang.smartgrow.dto.auth.request.SignUpRequestDTO;
import com.hoang.smartgrow.dto.auth.response.TokenResponseDTO;
import com.hoang.smartgrow.dto.auth.response.UserResponseDTO;
import com.hoang.smartgrow.exception.SmartGrowException;

public interface AuthService {
  TokenResponseDTO signIn(SignInRequestDTO signInRequestDTO) throws SmartGrowException;
  UserResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) throws SmartGrowException;
  TokenResponseDTO refresh(RefreshRequestDTO refreshRequestDTO) throws SmartGrowException;
}
