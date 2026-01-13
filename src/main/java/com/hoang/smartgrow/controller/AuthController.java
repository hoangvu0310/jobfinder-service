package com.hoang.smartgrow.controller;

import com.hoang.smartgrow.common.Const;
import com.hoang.smartgrow.dto.auth.request.RefreshRequestDTO;
import com.hoang.smartgrow.dto.auth.request.SignInRequestDTO;
import com.hoang.smartgrow.dto.auth.request.SignUpRequestDTO;
import com.hoang.smartgrow.dto.auth.response.TokenResponseDTO;
import com.hoang.smartgrow.dto.auth.response.UserResponseDTO;
import com.hoang.smartgrow.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Const.API_PREFIX + "/auth")
@AllArgsConstructor
public class AuthController {

  private AuthService authService;

  @PostMapping("/signIn")
  public ResponseEntity<TokenResponseDTO> signIn(@Valid @RequestBody SignInRequestDTO signInRequestDTO) {
    return ResponseEntity.ok(authService.signIn(signInRequestDTO));
  }

  @PostMapping("/signUp")
  public ResponseEntity<UserResponseDTO> signUp(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
    return ResponseEntity.ok(authService.signUp(signUpRequestDTO));
  }

  @PostMapping("/refresh")
  public ResponseEntity<TokenResponseDTO> refresh(@RequestBody RefreshRequestDTO refreshRequestDTO) {
    return ResponseEntity.ok(authService.refresh(refreshRequestDTO));
  }
}
