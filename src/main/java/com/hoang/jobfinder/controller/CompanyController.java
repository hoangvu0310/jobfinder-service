package com.hoang.jobfinder.controller;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.dto.ApiResponse;
import com.hoang.jobfinder.dto.company.request.CompanyInfoPostRequestDTO;
import com.hoang.jobfinder.dto.company.response.CompanyDTO;
import com.hoang.jobfinder.entity.company.CompanyDraftDTO;
import com.hoang.jobfinder.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Const.API_PREFIX + "/company")
@AllArgsConstructor
public class CompanyController {
  private CompanyService companyService;

  @PostMapping("/create")
  @PreAuthorize("hasRole('ROLE_HR_ADMIN')")
  public ResponseEntity<ApiResponse<CompanyDraftDTO>> createCompany(@RequestBody CompanyInfoPostRequestDTO requestDTO) {
    return ResponseEntity.ok(ApiResponse.successResponse(companyService.createCompany(requestDTO)));
  }

  @PatchMapping("/edit/{id}")
  @PreAuthorize("hasRole('ROLE_HR_ADMIN')")
  public ResponseEntity<ApiResponse<CompanyDraftDTO>> createCompany(
      @RequestBody CompanyInfoPostRequestDTO requestDTO, @PathVariable("id") Long companyId
  ) {
    return ResponseEntity.ok(ApiResponse.successResponse(companyService.editCompanyInfo(requestDTO, companyId)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<CompanyDTO>> createCompany(@PathVariable("id") Long companyId) {
    return ResponseEntity.ok(ApiResponse.successResponse(companyService.getCompanyInfo(companyId)));
  }
}
