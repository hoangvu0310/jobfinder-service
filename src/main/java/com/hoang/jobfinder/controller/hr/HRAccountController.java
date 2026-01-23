package com.hoang.jobfinder.controller.hr;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.dto.ApiResponse;
import com.hoang.jobfinder.dto.auth.request.HRSignUpRequestDTO;
import com.hoang.jobfinder.dto.auth.response.AccountInfoDTO;
import com.hoang.jobfinder.entity.HR;
import com.hoang.jobfinder.service.HRAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Const.HR_API_PREFIX + "/account")
@AllArgsConstructor
public class HRAccountController {
  private HRAccountService hrAccountService;

  @PostMapping("/createSubAccount")
  public ResponseEntity<ApiResponse<AccountInfoDTO>> createSubHRAccount(@RequestBody HRSignUpRequestDTO signUpRequestDTO) {
    return ResponseEntity.ok(ApiResponse.successResponse(hrAccountService.createSubHRAccount(signUpRequestDTO)));
  }

  @PatchMapping("/grantPermission/{id}")
  @PreAuthorize("hasRole('ROLE_HR_ADMIN')")
  public ResponseEntity<ApiResponse<AccountInfoDTO>> grantHRAdminPermission(@PathVariable("id") Long hrId) {
    return ResponseEntity.ok(ApiResponse.successResponse(hrAccountService.grantHRAdminPermission(hrId)));
  }

  @DeleteMapping("/deleteAccount/{id}")
  @PreAuthorize("hasRole('ROLE_HR_ADMIN')")
  public ResponseEntity<ApiResponse<Void>> deleteAccount(@PathVariable("id") Long hrId) {
    hrAccountService.deleteHRAccount(hrId);
    return ResponseEntity.ok(ApiResponse.successResponse());
  }

  @GetMapping("/getAllAccount/{companyId}")
  @PreAuthorize("hasRole('ROLE_HR_ADMIN')")
  public ResponseEntity<ApiResponse<List<HR>>> getAllAccount(@PathVariable Long companyId) {
    return ResponseEntity.ok(ApiResponse.successResponse(hrAccountService.getAllHRAccount(companyId)));
  }
}
