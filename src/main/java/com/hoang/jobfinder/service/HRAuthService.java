package com.hoang.jobfinder.service;

import com.hoang.jobfinder.dto.auth.request.HRSignUpRequestDTO;
import com.hoang.jobfinder.dto.auth.request.RefreshRequestDTO;
import com.hoang.jobfinder.dto.auth.request.SignInRequestDTO;
import com.hoang.jobfinder.dto.auth.response.AccountInfoDTO;
import com.hoang.jobfinder.dto.auth.response.TokenResponseDTO;

public interface HRAuthService {
  AccountInfoDTO signUpHRAccount(HRSignUpRequestDTO hrSignUpRequestDTO);
  TokenResponseDTO signIn(SignInRequestDTO signInRequestDTO);
  TokenResponseDTO refresh(RefreshRequestDTO refreshRequestDTO);
  void logout();
}
