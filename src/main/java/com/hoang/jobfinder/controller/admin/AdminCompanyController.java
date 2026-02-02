package com.hoang.jobfinder.controller.admin;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.dto.ApiResponse;
import com.hoang.jobfinder.dto.PageableResponse;
import com.hoang.jobfinder.dto.PagingDTO;
import com.hoang.jobfinder.dto.company.request.RejectRequestDTO;
import com.hoang.jobfinder.dto.company.response.CompanyDTO;
import com.hoang.jobfinder.dto.company.response.CompanyDraftDTO;
import com.hoang.jobfinder.service.AdminCompanyService;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Const.ADMIN_API_PREFIX + "/company")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@AllArgsConstructor
public class AdminCompanyController {
  private AdminCompanyService adminCompanyService;

  @GetMapping("/all")
  public ResponseEntity<ApiResponse<PageableResponse<CompanyDTO>>> getAllCompany(@RequestParam Integer page, @RequestParam Integer size) {
    return ResponseEntity.ok(ApiResponse.successResponse(
        adminCompanyService.getAllCompany(PagingDTO.builder().pageNumber(page).pageSize(size).build())
    ));
  }

  @GetMapping("/request")
  public ResponseEntity<ApiResponse<PageableResponse<CompanyDraftDTO>>> getAllCompanyRequest(
      @RequestParam Integer page, @RequestParam Integer size, @RequestParam @Nullable Enum.ActionType actionType
      ) {
    return ResponseEntity.ok(ApiResponse.successResponse(
        adminCompanyService.getCompanyRequestList(PagingDTO.builder().pageNumber(page).pageSize(size).build(), actionType)
    ));
  }

  @GetMapping("/request/{draftId}")
  public ResponseEntity<ApiResponse<CompanyDTO>> getCompanyRequestById(@PathVariable Long draftId) {
    return ResponseEntity.ok(ApiResponse.successResponse(
        adminCompanyService.getCompanyRequestById(draftId)
    ));
  }

  @PostMapping("/approveCreate/{draftId}")
  public ResponseEntity<ApiResponse<CompanyDTO>> approveCreateRequest(@PathVariable Long draftId) {
    return ResponseEntity.ok(ApiResponse.successResponse(
        adminCompanyService.approveCreateRequest(draftId)
    ));
  }

  @PatchMapping("/approveEdit/{draftId}")
  public ResponseEntity<ApiResponse<CompanyDTO>> approveEditRequest(@PathVariable Long draftId) {
    return ResponseEntity.ok(ApiResponse.successResponse(
        adminCompanyService.approveEditRequest(draftId)
    ));
  }

  @PatchMapping("/reject")
  public ResponseEntity<ApiResponse<CompanyDraftDTO>> rejectRequest(@RequestBody RejectRequestDTO rejectRequestDTO) {
    return ResponseEntity.ok(ApiResponse.successResponse(
        adminCompanyService.rejectRequest(rejectRequestDTO)
    ));
  }
}
