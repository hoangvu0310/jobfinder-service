package com.hoang.jobfinder.controller.hr;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.dto.ApiResponse;
import com.hoang.jobfinder.dto.auth.request.HRSignUpRequestDTO;
import com.hoang.jobfinder.dto.auth.request.RefreshRequestDTO;
import com.hoang.jobfinder.dto.auth.request.SignInRequestDTO;
import com.hoang.jobfinder.dto.auth.response.AccountInfoDTO;
import com.hoang.jobfinder.dto.auth.response.TokenResponseDTO;
import com.hoang.jobfinder.service.HRAuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Const.HR_API_PREFIX + "/auth")
@AllArgsConstructor
public class HRAuthController {
  private HRAuthService hrAuthService;

  @PostMapping("/signUp")
  public ResponseEntity<ApiResponse<AccountInfoDTO>> signUpHrAccount(@RequestBody HRSignUpRequestDTO signUpRequestDTO) {
    return ResponseEntity.ok(ApiResponse.successResponse(hrAuthService.signUpHRAccount(signUpRequestDTO)));
  }

  @PostMapping("/signIn")
  public ResponseEntity<ApiResponse<TokenResponseDTO>> signIn(@RequestBody SignInRequestDTO signInRequestDTO) {
    return ResponseEntity.ok(ApiResponse.successResponse(hrAuthService.signIn(signInRequestDTO)));
  }

  @PostMapping("/refresh")
  public ResponseEntity<ApiResponse<TokenResponseDTO>> refresh(@RequestBody RefreshRequestDTO refreshRequestDTO) {
    return ResponseEntity.ok(ApiResponse.successResponse(hrAuthService.refresh(refreshRequestDTO)));
  }

  @PostMapping("/logout")
  public ResponseEntity<ApiResponse<Void>> logout() {
    hrAuthService.logout();
    return ResponseEntity.ok(ApiResponse.successResponse());
  }
}
