package com.hoang.jobfinder.service;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.dto.PagingDTO;
import com.hoang.jobfinder.dto.company.request.RejectRequestDTO;
import com.hoang.jobfinder.dto.company.response.CompanyDTO;
import com.hoang.jobfinder.dto.company.response.CompanyDraftDTO;
import org.springframework.data.domain.Page;

public interface AdminCompanyService {
  Page<CompanyDTO> getAllCompany(PagingDTO pagingDTO);
  Page<CompanyDraftDTO> getCompanyRequestList(PagingDTO pagingDTO, Enum.ActionType actionType);
  CompanyDTO getCompanyRequestById(Long draftId);
  CompanyDTO approveCreateRequest(Long draftId);
  CompanyDraftDTO rejectRequest(RejectRequestDTO rejectRequestDTO);
  CompanyDTO approveEditRequest(Long draftId);
}
