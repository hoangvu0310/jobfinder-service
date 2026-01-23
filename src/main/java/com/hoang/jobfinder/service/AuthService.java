package com.hoang.jobfinder.service;

import com.hoang.jobfinder.dto.auth.request.RefreshRequestDTO;
import com.hoang.jobfinder.dto.auth.request.SignInRequestDTO;
import com.hoang.jobfinder.dto.auth.request.SignUpRequestDTO;
import com.hoang.jobfinder.dto.auth.response.TokenResponseDTO;
import com.hoang.jobfinder.dto.auth.response.AccountInfoDTO;
import com.hoang.jobfinder.exception.JobFinderException;

public interface AuthService {
  TokenResponseDTO signIn(SignInRequestDTO signInRequestDTO) throws JobFinderException;
  AccountInfoDTO signUp(SignUpRequestDTO signUpRequestDTO) throws JobFinderException;
  TokenResponseDTO refresh(RefreshRequestDTO refreshRequestDTO) throws JobFinderException;
  AccountInfoDTO getUserInfo();
  void logout() throws JobFinderException;
  TokenResponseDTO guest(String deviceId);
  void guestToUser(SignUpRequestDTO signUpRequestDTO);
}
