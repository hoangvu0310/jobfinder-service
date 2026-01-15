package com.hoang.jobfinder.service.impl;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.common.ResultCode;
import com.hoang.jobfinder.config.security.JwtService;
import com.hoang.jobfinder.config.security.PasswordEncoderService;
import com.hoang.jobfinder.dto.auth.request.RefreshRequestDTO;
import com.hoang.jobfinder.dto.auth.request.SignInRequestDTO;
import com.hoang.jobfinder.dto.auth.request.SignUpRequestDTO;
import com.hoang.jobfinder.dto.auth.response.TokenResponseDTO;
import com.hoang.jobfinder.dto.auth.response.UserInfoDTO;
import com.hoang.jobfinder.entity.User;
import com.hoang.jobfinder.exception.JobFinderException;
import com.hoang.jobfinder.repository.UserRepository;
import com.hoang.jobfinder.service.AuthService;
import com.hoang.jobfinder.service.RefreshTokenService;
import com.hoang.jobfinder.util.UserUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

  private UserRepository userRepository;

  private PasswordEncoderService passwordEncoderService;

  private JwtService jwtService;

  private RefreshTokenService refreshTokenService;

  @Override
  public TokenResponseDTO signIn(SignInRequestDTO signInRequestDTO) throws JobFinderException {
    try {
      Optional<User> userOptional = userRepository.findUserByUsername(signInRequestDTO.getUsername());

      if (userOptional.isPresent()) {
        User user = userOptional.get();

        if (passwordEncoderService.matches(signInRequestDTO.getPassword(), user.getPassword())) {
          String accessToken = jwtService.generateToken(user);
          String refreshToken = refreshTokenService.createRefreshToken(user);
          UserInfoDTO userDTO = jwtService.getTokenPayload(accessToken);

          return TokenResponseDTO.builder()
              .accessToken(accessToken)
              .refreshToken(refreshToken)
              .user(userDTO)
              .build();
        } else {
          throw new JobFinderException(ResultCode.INVALID_CREDENTIALS);
        }
      } else {
        throw new JobFinderException(ResultCode.INVALID_CREDENTIALS);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  @Override
  public UserInfoDTO signUp(SignUpRequestDTO signUpRequestDTO) throws JobFinderException {
    try {
      boolean isUserExisted = userRepository.existsUserByUsername(signUpRequestDTO.getUsername());

      if (isUserExisted) {
        throw new JobFinderException(ResultCode.EXISTED_USER);
      }

      User newUser = User.builder()
          .role(Enum.Role.EMPLOYER)
          .fullName(signUpRequestDTO.getFullName())
          .username(signUpRequestDTO.getUsername())
          .password(passwordEncoderService.encodePassword(signUpRequestDTO.getPassword()))
          .email(signUpRequestDTO.getEmail())
          .phoneNumber(signUpRequestDTO.getPhoneNumber())
          .createdBy(signUpRequestDTO.getUsername())
          .build();

      userRepository.save(newUser);

      return UserInfoDTO.builder()
          .userId(newUser.getUserId())
          .username(newUser.getUsername())
          .email(newUser.getEmail())
          .phoneNumber(newUser.getPhoneNumber())
          .fullName(newUser.getFullName())
          .role(newUser.getRole())
          .build();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  @Override
  public TokenResponseDTO refresh(RefreshRequestDTO refreshRequestDTO) throws JobFinderException {
    try {
      User currentUser = userRepository.findUserByUserId(refreshRequestDTO.getUserId());
      String newRefreshToken = refreshTokenService.refresh(refreshRequestDTO);
      String newAccessToken = jwtService.generateToken(currentUser);

      return TokenResponseDTO.builder()
          .accessToken(newAccessToken)
          .refreshToken(newRefreshToken)
          .user(
              UserInfoDTO.builder()
                  .userId(currentUser.getUserId())
                  .username(currentUser.getUsername())
                  .email(currentUser.getEmail())
                  .phoneNumber(currentUser.getPhoneNumber())
                  .fullName(currentUser.getFullName())
                  .role(currentUser.getRole())
                  .build()
          )
          .build();
    } catch (JobFinderException e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  @Override
  public UserInfoDTO getUserInfo() {
    return UserUtil.getCurrentUser() ;
  }

  @Override
  public void logout() throws JobFinderException {
    UserInfoDTO userInfoDTO = UserUtil.getCurrentUser();

    if (userInfoDTO != null) {
      refreshTokenService.deleteToken(userInfoDTO.getUserId());
    } else {
      throw new JobFinderException(ResultCode.INTERNAL_ERROR);
    }

  }
}