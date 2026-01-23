package com.hoang.jobfinder.service.impl;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.common.ErrorCode;
import com.hoang.jobfinder.common.ResultCode;
import com.hoang.jobfinder.config.security.PasswordEncoderService;
import com.hoang.jobfinder.dto.auth.request.HRSignUpRequestDTO;
import com.hoang.jobfinder.dto.auth.response.AccountInfoDTO;
import com.hoang.jobfinder.entity.Company;
import com.hoang.jobfinder.entity.HR;
import com.hoang.jobfinder.exception.JobFinderException;
import com.hoang.jobfinder.repository.CompanyRepository;
import com.hoang.jobfinder.repository.HRRepository;
import com.hoang.jobfinder.service.HRAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class HRAccountServiceImpl implements HRAccountService {
  private PasswordEncoderService passwordEncoderService;

  private HRRepository hrRepository;

  private CompanyRepository companyRepository;

  @Override
  public AccountInfoDTO createSubHRAccount(HRSignUpRequestDTO hrSignUpRequestDTO) throws JobFinderException {
    boolean isUserExisted = hrRepository.existsHRByEmail(hrSignUpRequestDTO.getEmail());

    if (isUserExisted) {
      throw new JobFinderException(ResultCode.EXISTED_USER);
    }

    Company company = companyRepository.findCompanyByCompanyId(hrSignUpRequestDTO.getCompanyId()).get(0);

    HR newHR = HR.builder()
        .role(Enum.Role.HR_ADMIN)
        .password(passwordEncoderService.encodePassword(hrSignUpRequestDTO.getPassword()))
        .email(hrSignUpRequestDTO.getEmail())
        .authType(Enum.AuthType.EMAIL_AND_PASSWORD)
        .createdBy(hrSignUpRequestDTO.getEmail())
        .company(company)
        .build();

    hrRepository.save(newHR);

    return AccountInfoDTO.fromUser(newHR);
  }

  @Override
  @Transactional
  public AccountInfoDTO grantHRAdminPermission(Long userId) {
    HR hr = hrRepository.findHRById(userId);
    hr.setRole(Enum.Role.HR_ADMIN);

    return AccountInfoDTO.fromUser(hr);
  }

  @Override
  @Transactional
  public void deleteHRAccount(Long id) throws JobFinderException {
    HR hr = hrRepository.findHRById(id);

    if (hr == null) {
      throw new JobFinderException(ErrorCode.BAD_REQUEST, "Không tìm thấy người dùng, tài khoản có thể đã bị xoá trước đó");
    }

    if (hr.getRole().equals(Enum.Role.HR_ADMIN)) {
      throw new JobFinderException(ErrorCode.BAD_REQUEST, "Không thể xoá tài khoản HR Admin");
    }

    hrRepository.deleteHRById(id);
  }

  @Override
  public List<HR> getAllHRAccount(Long companyId) {
    return hrRepository.findHRByCompanyId(companyId);
  }
}
