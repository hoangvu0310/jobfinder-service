package com.hoang.jobfinder.service;

import com.hoang.jobfinder.dto.FileTypeDTO;
import com.hoang.jobfinder.dto.UploadUrlResponseDTO;
import com.hoang.jobfinder.dto.cv.CVResponseDTO;

import java.util.List;

public interface CVService {
  List<CVResponseDTO> getAllCV();
  CVResponseDTO getById(Long cvId);
  UploadUrlResponseDTO generateCVUploadUrl(FileTypeDTO fileTypeDTO);
  void saveNewCV(String cvKey);
  void updateCV(Long cvId);
  void deleteCV(Long cvId);
}
