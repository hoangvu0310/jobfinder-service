package com.hoang.jobfinder.controller;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.dto.ApiResponse;
import com.hoang.jobfinder.dto.profile.request.UserProfileEditRequestDTO;
import com.hoang.jobfinder.dto.profile.response.UserProfileResponseDTO;
import com.hoang.jobfinder.service.UserProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Const.API_PREFIX + "/userProfile")
@AllArgsConstructor
public class UserProfileController {
  private UserProfileService userProfileService;

  @GetMapping()
  public ResponseEntity<ApiResponse<UserProfileResponseDTO>> getUserProfile() {
    return ResponseEntity.ok(ApiResponse.successResponse(userProfileService.findProfileByUserId()));
  }

  @PatchMapping()
  public ResponseEntity<ApiResponse<UserProfileResponseDTO>> editUserProfile(@RequestBody UserProfileEditRequestDTO editRequestDTO) {
    return ResponseEntity.ok(ApiResponse.successResponse(userProfileService.editProfile(editRequestDTO)));
  }
}
