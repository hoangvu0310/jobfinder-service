package com.hoang.jobfinder.controller;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.dto.ApiResponse;
import com.hoang.jobfinder.dto.auth.request.RefreshRequestDTO;
import com.hoang.jobfinder.dto.auth.request.SignInRequestDTO;
import com.hoang.jobfinder.dto.auth.request.SignUpRequestDTO;
import com.hoang.jobfinder.dto.auth.response.TokenResponseDTO;
import com.hoang.jobfinder.dto.auth.response.AccountInfoDTO;
import com.hoang.jobfinder.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Const.API_PREFIX + "/auth")
@AllArgsConstructor
public class AuthController {

  private AuthService authService;

  @PostMapping("/signIn")
  public ResponseEntity<ApiResponse<TokenResponseDTO>> signIn(@Valid @RequestBody SignInRequestDTO signInRequestDTO) {
    return ResponseEntity.ok(ApiResponse.successResponse(authService.signIn(signInRequestDTO)));
  }

  @PostMapping("/signUp")
  public ResponseEntity<ApiResponse<AccountInfoDTO>> signUp(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
    return ResponseEntity.ok(ApiResponse.successResponse(authService.signUp(signUpRequestDTO)));
  }

  @PostMapping("/refresh")
  public ResponseEntity<ApiResponse<TokenResponseDTO>> refresh(@RequestBody RefreshRequestDTO refreshRequestDTO) {
    return ResponseEntity.ok(ApiResponse.successResponse(authService.refresh(refreshRequestDTO)));
  }

  @GetMapping("userInfo")
  public ResponseEntity<ApiResponse<AccountInfoDTO>> getUserInfo() {
    return ResponseEntity.ok(ApiResponse.successResponse(authService.getUserInfo()));
  }

  @PostMapping("/logout")
  public ResponseEntity<ApiResponse<Void>> logout() {
    authService.logout();
    return ResponseEntity.ok(ApiResponse.successResponse());
  }

  @PostMapping("/guest/{deviceId}")
  public ResponseEntity<ApiResponse<TokenResponseDTO>> guest(@PathVariable String deviceId) {
    return ResponseEntity.ok(ApiResponse.successResponse(authService.guest(deviceId)));
  }

  @PreAuthorize("hasRole('GUEST')")
  @PostMapping("/guestToUser")
  public ResponseEntity<ApiResponse<Void>> guestToUser(@RequestBody SignUpRequestDTO signUpRequestDTO) {
    authService.guestToUser(signUpRequestDTO);
    return ResponseEntity.ok(ApiResponse.successResponse());
  }
}
