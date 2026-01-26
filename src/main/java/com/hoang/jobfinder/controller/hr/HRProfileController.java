package com.hoang.jobfinder.controller.hr;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.dto.ApiResponse;
import com.hoang.jobfinder.dto.profile.request.HRProfileEditRequestDTO;
import com.hoang.jobfinder.dto.profile.response.HRProfileResponseDTO;
import com.hoang.jobfinder.service.HRProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Const.HR_API_PREFIX + "/profile")
@AllArgsConstructor
public class HRProfileController {
  private HRProfileService hrProfileService;

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<HRProfileResponseDTO>> getProfileById(@PathVariable Long id) {
    return ResponseEntity.ok(ApiResponse.successResponse(hrProfileService.findProfileByHRId(id)));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ApiResponse<HRProfileResponseDTO>> editProfile(
      @RequestBody HRProfileEditRequestDTO editRequestDTO,
      @PathVariable Long id
  ) {
    return ResponseEntity.ok(ApiResponse.successResponse(hrProfileService.editProfile(editRequestDTO, id)));
  }
}
