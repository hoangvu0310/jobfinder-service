package com.hoang.jobfinder.service;

import com.hoang.jobfinder.dto.auth.request.RefreshRequestDTO;
import com.hoang.jobfinder.dto.auth.request.SignInRequestDTO;
import com.hoang.jobfinder.dto.auth.request.SignUpRequestDTO;
import com.hoang.jobfinder.dto.auth.response.TokenResponseDTO;
import com.hoang.jobfinder.dto.auth.response.UserInfoDTO;
import com.hoang.jobfinder.exception.JobFinderException;

public interface AuthService {
  TokenResponseDTO signIn(SignInRequestDTO signInRequestDTO) throws JobFinderException;
  UserInfoDTO signUp(SignUpRequestDTO signUpRequestDTO) throws JobFinderException;
  TokenResponseDTO refresh(RefreshRequestDTO refreshRequestDTO) throws JobFinderException;
  UserInfoDTO getUserInfo();
  void logout() throws JobFinderException;
}
