package com.hoang.jobfinder.service.impl;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.common.ResultCode;
import com.hoang.jobfinder.config.security.JwtService;
import com.hoang.jobfinder.config.security.PasswordEncoderService;
import com.hoang.jobfinder.dto.auth.request.HRSignUpRequestDTO;
import com.hoang.jobfinder.dto.auth.request.RefreshRequestDTO;
import com.hoang.jobfinder.dto.auth.request.SignInRequestDTO;
import com.hoang.jobfinder.dto.auth.response.AccountInfoDTO;
import com.hoang.jobfinder.dto.auth.response.TokenResponseDTO;
import com.hoang.jobfinder.entity.Company;
import com.hoang.jobfinder.entity.HR;
import com.hoang.jobfinder.entity.HRProfile;
import com.hoang.jobfinder.exception.JobFinderException;
import com.hoang.jobfinder.repository.CompanyRepository;
import com.hoang.jobfinder.repository.HRProfileRepository;
import com.hoang.jobfinder.repository.HRRepository;
import com.hoang.jobfinder.service.HRAuthService;
import com.hoang.jobfinder.service.RefreshTokenService;
import com.hoang.jobfinder.util.UserUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class HRAuthServiceImpl implements HRAuthService {
  private PasswordEncoderService passwordEncoderService;

  private JwtService jwtService;

  private RefreshTokenService refreshTokenService;

  private HRRepository hrRepository;

  private CompanyRepository companyRepository;

  private HRProfileRepository hrProfileRepository;

  private final Boolean isHR = true;

  @Override
  @Transactional
  public AccountInfoDTO signUpHRAccount(HRSignUpRequestDTO hrSignUpRequestDTO) throws JobFinderException {
    boolean isUserExisted = hrRepository.existsHRByEmail(hrSignUpRequestDTO.getEmail());
    Company company = null;

    if (isUserExisted) {
      throw new JobFinderException(ResultCode.EXISTED_USER);
    }

    if (hrSignUpRequestDTO.getCompanyId() != null) {
      company = companyRepository.findCompanyByCompanyId(hrSignUpRequestDTO.getCompanyId()).get(0);
    }

    HR newHR = HR.builder()
        .role(com.hoang.jobfinder.common.Enum.Role.HR_ADMIN)
        .password(passwordEncoderService.encodePassword(hrSignUpRequestDTO.getPassword()))
        .email(hrSignUpRequestDTO.getEmail())
        .authType(Enum.AuthType.EMAIL_AND_PASSWORD)
        .createdBy(hrSignUpRequestDTO.getEmail())
        .company(company)
        .build();

    HRProfile hrProfile = HRProfile.builder()
        .fullName(hrSignUpRequestDTO.getFullName())
        .email(hrSignUpRequestDTO.getEmail())
        .build();

    hrRepository.save(newHR);
    hrProfileRepository.save(hrProfile);

    return AccountInfoDTO.fromUser(newHR);
  }

  @Override
  @Transactional
  public TokenResponseDTO signIn(SignInRequestDTO signInRequestDTO) throws JobFinderException {
    HR hr = hrRepository
        .findHRByEmail(signInRequestDTO.getEmail())
        .orElseThrow(() -> new JobFinderException(ResultCode.INVALID_CREDENTIALS));

    if (passwordEncoderService.matches(signInRequestDTO.getPassword(), hr.getPassword())) {
      String accessToken = jwtService.generateToken(hr);
      String refreshToken = refreshTokenService.createRefreshToken(
          hr,
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
  }

  @Override
  public TokenResponseDTO refresh(RefreshRequestDTO refreshRequestDTO) {
    try {
      HR currentHR = hrRepository.findHRById(refreshRequestDTO.getUserId());
      String newRefreshToken = refreshTokenService.refresh(refreshRequestDTO, isHR);
      String newAccessToken = jwtService.generateToken(currentHR);

      return TokenResponseDTO.builder()
          .accessToken(newAccessToken)
          .refreshToken(newRefreshToken)
          .user(AccountInfoDTO.fromUser(currentHR))
          .build();
    } catch (JobFinderException e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  @Override
  public void logout() {
    AccountInfoDTO accountInfoDTO = UserUtil.getCurrentUser();

    if (accountInfoDTO != null) {
      refreshTokenService.deleteToken(accountInfoDTO.getUserId(), isHR);
    } else {
      throw new JobFinderException(ResultCode.INTERNAL_ERROR);
    }
  }
}
