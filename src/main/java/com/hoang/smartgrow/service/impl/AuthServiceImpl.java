package com.hoang.smartgrow.service.impl;

import com.hoang.smartgrow.common.ResultCode;
import com.hoang.smartgrow.common.Role;
import com.hoang.smartgrow.config.security.JwtService;
import com.hoang.smartgrow.config.security.PasswordEncoderService;
import com.hoang.smartgrow.dto.auth.UserTokenPayloadDTO;
import com.hoang.smartgrow.dto.auth.request.RefreshRequestDTO;
import com.hoang.smartgrow.dto.auth.request.SignInRequestDTO;
import com.hoang.smartgrow.dto.auth.request.SignUpRequestDTO;
import com.hoang.smartgrow.dto.auth.response.TokenResponseDTO;
import com.hoang.smartgrow.dto.auth.response.UserResponseDTO;
import com.hoang.smartgrow.entity.User;
import com.hoang.smartgrow.exception.SmartGrowException;
import com.hoang.smartgrow.repository.UserRepository;
import com.hoang.smartgrow.service.AuthService;
import com.hoang.smartgrow.service.RefreshTokenService;
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
  public TokenResponseDTO signIn(SignInRequestDTO signInRequestDTO) throws SmartGrowException {
    try {
      Optional<User> userOptional = userRepository.findUserByUsername(signInRequestDTO.getUsername());

      if (userOptional.isPresent()) {
        User user = userOptional.get();

        if (passwordEncoderService.matches(signInRequestDTO.getPassword(), user.getPassword())) {
          String accessToken = jwtService.generateToken(user);
          String refreshToken = refreshTokenService.createRefreshToken(user);

          return TokenResponseDTO.builder()
              .accessToken(accessToken)
              .refreshToken(refreshToken)
              .user(jwtService.getTokenPayload(accessToken))
              .build();
        } else {
          throw new SmartGrowException(ResultCode.INVALID_CREDENTIALS);
        }
      } else {
        throw new SmartGrowException(ResultCode.INVALID_CREDENTIALS);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  @Override
  public UserResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) throws SmartGrowException {
    try {
      Optional<User> existedUser = userRepository.findUserByUsername(signUpRequestDTO.getUsername());

      if (existedUser.isPresent()) {
        throw new SmartGrowException(ResultCode.EXISTED_USER);
      }

      User newUser = User.builder()
          .role(Role.EMPLOYER)
          .fullName(signUpRequestDTO.getFullName())
          .username(signUpRequestDTO.getUsername())
          .password(passwordEncoderService.encodePassword(signUpRequestDTO.getPassword()))
          .email(signUpRequestDTO.getEmail())
          .phoneNumber(signUpRequestDTO.getPhoneNumber())
          .createdBy(signUpRequestDTO.getUsername())
          .build();

      userRepository.save(newUser);

      return UserResponseDTO.builder()
          .userId(newUser.getUserId())
          .username(newUser.getUsername())
          .email(newUser.getEmail())
          .phoneNumber(newUser.getPhoneNumber())
          .fullName(newUser.getFullName())
          .role(newUser.getRole())
          .createdAt(newUser.getCreatedAt())
          .createBy(newUser.getCreatedBy())
          .build();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  @Override
  public TokenResponseDTO refresh(RefreshRequestDTO refreshRequestDTO) throws SmartGrowException {
    try {
      User currentUser = userRepository.findUserByUserId(refreshRequestDTO.getUserId());
      String newRefreshToken = refreshTokenService.refresh(refreshRequestDTO);
      String newAccessToken  = jwtService.generateToken(currentUser);

      return TokenResponseDTO.builder()
          .accessToken(newAccessToken)
          .refreshToken(newRefreshToken)
          .user(
              UserTokenPayloadDTO.builder()
                  .userId(currentUser.getUserId())
                  .username(currentUser.getUsername())
                  .fullName(currentUser.getFullName())
                  .role(currentUser.getRole())
                  .build()
          )
          .build();
    } catch (SmartGrowException e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }
}