package com.hoang.jobfinder.controller;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.dto.ApiResponse;
import com.hoang.jobfinder.dto.FileTypeDTO;
import com.hoang.jobfinder.dto.UploadUrlResponseDTO;
import com.hoang.jobfinder.dto.cv.CVKeyDTO;
import com.hoang.jobfinder.dto.cv.CVResponseDTO;
import com.hoang.jobfinder.service.CVService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Const.API_PREFIX + "/cv")
@AllArgsConstructor
@PreAuthorize("hasRole('ROLE_USER')")
public class CVController {
  private CVService cvService;

  @GetMapping("/all")
  private ResponseEntity<ApiResponse<List<CVResponseDTO>>> getAllCV() {
    return ResponseEntity.ok(ApiResponse.successResponse(cvService.getAllCV()));
  }

  @GetMapping("/detail/{id}")
  private ResponseEntity<ApiResponse<CVResponseDTO>> getCVById(@PathVariable Long id) {
    return ResponseEntity.ok(ApiResponse.successResponse(cvService.getById(id)));
  }

  @PostMapping("/uploadUrl")
  private ResponseEntity<ApiResponse<UploadUrlResponseDTO>> generateCVUploadUrl(@RequestBody FileTypeDTO fileTypeDTO) {
    return ResponseEntity.ok(ApiResponse.successResponse(cvService.generateCVUploadUrl(fileTypeDTO)));
  }

  @PostMapping("/save")
  private ResponseEntity<ApiResponse<Void>> saveNewCV(@RequestBody CVKeyDTO cvKeyDTO) {
    cvService.saveNewCV(cvKeyDTO.getCvKey());
    return ResponseEntity.ok(ApiResponse.successResponse());
  }

  @PatchMapping("/edit/{id}")
  private ResponseEntity<ApiResponse<Void>> updateCV(@PathVariable Long id) {
    cvService.updateCV(id);
    return ResponseEntity.ok(ApiResponse.successResponse());
  }

  @DeleteMapping("/delete/{id}")
  private ResponseEntity<ApiResponse<Void>> deleteCV(@PathVariable Long id) {
    cvService.deleteCV(id);
    return ResponseEntity.ok(ApiResponse.successResponse());
  }
}
