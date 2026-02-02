package com.hoang.jobfinder.service;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.dto.PageableResponse;
import com.hoang.jobfinder.dto.PagingDTO;
import com.hoang.jobfinder.dto.company.request.RejectRequestDTO;
import com.hoang.jobfinder.dto.company.response.CompanyDTO;
import com.hoang.jobfinder.dto.company.response.CompanyDraftDTO;

public interface AdminCompanyService {
  PageableResponse<CompanyDTO> getAllCompany(PagingDTO pagingDTO);
  PageableResponse<CompanyDraftDTO> getCompanyRequestList(PagingDTO pagingDTO, Enum.ActionType actionType);
  CompanyDTO getCompanyRequestById(Long draftId);
  CompanyDTO approveCreateRequest(Long draftId);
  CompanyDraftDTO rejectRequest(RejectRequestDTO rejectRequestDTO);
  CompanyDTO approveEditRequest(Long draftId);
}
