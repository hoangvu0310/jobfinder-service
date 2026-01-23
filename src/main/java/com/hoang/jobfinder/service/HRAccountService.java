package com.hoang.jobfinder.service;

import com.hoang.jobfinder.dto.auth.request.HRSignUpRequestDTO;
import com.hoang.jobfinder.dto.auth.response.AccountInfoDTO;
import com.hoang.jobfinder.entity.HR;

import java.util.List;

public interface HRAccountService {
  AccountInfoDTO createSubHRAccount(HRSignUpRequestDTO hrSignUpRequestDTO);
  AccountInfoDTO grantHRAdminPermission(Long userId);
  void deleteHRAccount(Long userId);
  List<HR> getAllHRAccount(Long companyId);

}
