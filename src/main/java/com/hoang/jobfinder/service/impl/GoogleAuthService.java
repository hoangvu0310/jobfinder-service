package com.hoang.jobfinder.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.common.ErrorCode;
import com.hoang.jobfinder.config.security.JwtService;
import com.hoang.jobfinder.dto.auth.request.SocialAuthRequestDTO;
import com.hoang.jobfinder.dto.auth.response.TokenResponseDTO;
import com.hoang.jobfinder.dto.auth.response.AccountInfoDTO;
import com.hoang.jobfinder.entity.user.User;
import com.hoang.jobfinder.exception.JobFinderException;
import com.hoang.jobfinder.repository.UserRepository;
import com.hoang.jobfinder.service.RefreshTokenService;
import com.hoang.jobfinder.service.SocialAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@Service
public class GoogleAuthService implements SocialAuthService {
  private final UserRepository userRepository;

  private final JwtService jwtService;

  private final RefreshTokenService refreshTokenService;

  private final GoogleIdTokenVerifier idTokenVerifier;


  @Autowired
  public GoogleAuthService(UserRepository userRepository, JwtService jwtService, RefreshTokenService refreshTokenService) {
    this.userRepository = userRepository;
    this.jwtService = jwtService;
    this.refreshTokenService = refreshTokenService;

    GoogleClientSecrets clientSecrets;
    {
      try {
        clientSecrets = GoogleClientSecrets.load(
            GsonFactory.getDefaultInstance(),
            new FileReader("src/main/resources/google-oauth-client.json")
        );
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    idTokenVerifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
        .setAudience(Collections.singletonList(clientSecrets.getDetails().getClientId()))
        .build();
  }

  @Override
  public Enum.AuthType authType() {
    return Enum.AuthType.GOOGLE;
  }

  @Override
  public TokenResponseDTO socialAccountAuthenticate(SocialAuthRequestDTO socialAuthRequestDTO) throws GeneralSecurityException, IOException, JobFinderException {
    GoogleIdToken googleIdToken = idTokenVerifier.verify(socialAuthRequestDTO.getIdToken());

    if (googleIdToken == null) {
      throw new JobFinderException(ErrorCode.INVALID_CREDENTIALS, "Xác thực tài khoản Google lỗi");
    }

    GoogleIdToken.Payload payload = googleIdToken.getPayload();
    String email = payload.getEmail();
    Optional<User> optionalUser = userRepository.findUserByEmail(email);

    if (optionalUser.isPresent()) {
      User user = optionalUser.get();

      if (user.getAuthType().equals(Enum.AuthType.GOOGLE)) {
        String accessToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user, socialAuthRequestDTO.getDeviceId(), socialAuthRequestDTO.getPlatform(), false);
        AccountInfoDTO userDTO = jwtService.getTokenPayload(accessToken);

        return TokenResponseDTO.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .user(userDTO)
            .build();
      } else {
        throw new JobFinderException(ErrorCode.EXISTED_USER, "Đã tồn tại tài khoản với email này");
      }
    } else {
      User newUser = User.builder()
          .role(Enum.Role.USER)
          .authType(Enum.AuthType.GOOGLE)
          .email(payload.getEmail())
          .build();
      String accessToken = jwtService.generateToken(newUser);
      String refreshToken = refreshTokenService.createRefreshToken(newUser, socialAuthRequestDTO.getDeviceId(), socialAuthRequestDTO.getPlatform(), false);

      userRepository.save(newUser);
      return TokenResponseDTO.builder()
          .accessToken(accessToken)
          .refreshToken(refreshToken)
          .user(AccountInfoDTO.fromUser(newUser))
          .build();
    }
  }
}
