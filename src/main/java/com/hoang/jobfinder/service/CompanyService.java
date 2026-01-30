package com.hoang.jobfinder.service;

import com.hoang.jobfinder.dto.company.response.CompanyDTO;
import com.hoang.jobfinder.dto.company.request.CompanyInfoPostRequestDTO;
import com.hoang.jobfinder.dto.company.response.CompanyDraftDTO;

public interface CompanyService {
  CompanyDraftDTO createCompany(CompanyInfoPostRequestDTO requestDTO);
  CompanyDraftDTO editCompanyInfo(CompanyInfoPostRequestDTO requestDTO, Long companyId);
  CompanyDTO getCompanyInfo(Long companyId);
}
