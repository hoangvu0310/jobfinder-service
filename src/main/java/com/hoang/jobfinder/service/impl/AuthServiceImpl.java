package com.hoang.jobfinder.service.impl;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.common.ResultCode;
import com.hoang.jobfinder.config.security.JwtService;
import com.hoang.jobfinder.config.security.PasswordEncoderService;
import com.hoang.jobfinder.dto.auth.request.RefreshRequestDTO;
import com.hoang.jobfinder.dto.auth.request.SignInRequestDTO;
import com.hoang.jobfinder.dto.auth.request.SignUpRequestDTO;
import com.hoang.jobfinder.dto.auth.response.AccountInfoDTO;
import com.hoang.jobfinder.dto.auth.response.TokenResponseDTO;
import com.hoang.jobfinder.entity.user.User;
import com.hoang.jobfinder.entity.user.UserProfile;
import com.hoang.jobfinder.exception.JobFinderException;
import com.hoang.jobfinder.repository.UserProfileRepository;
import com.hoang.jobfinder.repository.UserRepository;
import com.hoang.jobfinder.service.AuthService;
import com.hoang.jobfinder.service.RefreshTokenService;
import com.hoang.jobfinder.util.UserUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

  private UserRepository userRepository;

  private UserProfileRepository userProfileRepository;

  private PasswordEncoderService passwordEncoderService;

  private JwtService jwtService;

  private RefreshTokenService refreshTokenService;

  private final Boolean isHR = false;

  @Override
  @Transactional
  public TokenResponseDTO signIn(SignInRequestDTO signInRequestDTO) throws JobFinderException {
    try {
      User user = userRepository
          .findUserByEmail(signInRequestDTO.getEmail())
          .orElseThrow(() -> new JobFinderException(ResultCode.INVALID_CREDENTIALS));

      if (passwordEncoderService.matches(signInRequestDTO.getPassword(), user.getPassword())) {
        String accessToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(
            user,
            signInRequestDTO.getDeviceId(),
            signInRequestDTO.getPlatform(),
            isHR
        );
        AccountInfoDTO userDTO = jwtService.getTokenPayload(accessToken);

        return TokenResponseDTO.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .user(userDTO)
            .build();

      } else {
        throw new JobFinderException(ResultCode.INVALID_CREDENTIALS);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  @Override
  @Transactional
  public AccountInfoDTO signUp(SignUpRequestDTO signUpRequestDTO) throws JobFinderException {
    try {
      boolean isUserExisted = userRepository.existsUserByEmail(signUpRequestDTO.getEmail());

      if (isUserExisted) {
        throw new JobFinderException(ResultCode.EXISTED_USER);
      }

      UserProfile newProfile = UserProfile.builder()
          .fullName(signUpRequestDTO.getFullName())
          .email(signUpRequestDTO.getEmail())
          .createdBy(signUpRequestDTO.getEmail())
          .build();

      User newUser = User.builder()
          .role(Enum.Role.USER)
          .password(passwordEncoderService.encodePassword(signUpRequestDTO.getPassword()))
          .email(signUpRequestDTO.getEmail())
          .authType(Enum.AuthType.EMAIL_AND_PASSWORD)
          .userProfile(newProfile)
          .createdBy(signUpRequestDTO.getEmail())
          .build();

      userRepository.save(newUser);
      userProfileRepository.save(newProfile);

      return AccountInfoDTO.fromUser(newUser);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  @Override
  public TokenResponseDTO refresh(RefreshRequestDTO refreshRequestDTO) throws JobFinderException {
    try {
      User currentUser = userRepository.findUserById(refreshRequestDTO.getUserId());
      String newRefreshToken = refreshTokenService.refresh(refreshRequestDTO, isHR);
      String newAccessToken = jwtService.generateToken(currentUser);

      return TokenResponseDTO.builder()
          .accessToken(newAccessToken)
          .refreshToken(newRefreshToken)
          .user(AccountInfoDTO.fromUser(currentUser))
          .build();
    } catch (JobFinderException e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  @Override
  public AccountInfoDTO getUserInfo() {
    return UserUtil.getCurrentUser();
  }

  @Override
  public void logout() throws JobFinderException {
    AccountInfoDTO accountInfoDTO = UserUtil.getCurrentUser();

    if (accountInfoDTO != null) {
      refreshTokenService.deleteToken(accountInfoDTO.getUserId(), isHR);
    } else {
      throw new JobFinderException(ResultCode.INTERNAL_ERROR);
    }

  }

  @Override
  public TokenResponseDTO guest(String deviceId) {
    UUID guestId = UUID.randomUUID();
    User guestUser = User.builder()
        .email("guest_" + guestId + "@gmail.com")
        .role(Enum.Role.GUEST)
        .build();

    userRepository.save(guestUser);

    String accessToken = jwtService.generateToken(guestUser);
    String refreshToken = refreshTokenService.createRefreshToken(guestUser, deviceId, null, isHR);

    return TokenResponseDTO.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .user(AccountInfoDTO.fromUser(guestUser))
        .build();
  }

  @Override
  @Transactional
  public void guestToUser(SignUpRequestDTO signUpRequestDTO) throws JobFinderException {
    AccountInfoDTO guestUserInfo = UserUtil.getCurrentUser();

    if (guestUserInfo != null) {
      User guestUser = userRepository.findUserById(guestUserInfo.getUserId());

      if (userRepository.existsUserByEmail(signUpRequestDTO.getEmail())) {
        throw new JobFinderException(ResultCode.EXISTED_USER);
      }

      guestUser.setPassword(passwordEncoderService.encodePassword(signUpRequestDTO.getPassword()));
      guestUser.setEmail(signUpRequestDTO.getEmail());
      guestUser.setRole(Enum.Role.USER);
    }
  }
}