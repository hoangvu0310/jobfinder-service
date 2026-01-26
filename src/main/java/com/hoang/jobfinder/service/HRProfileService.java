package com.hoang.jobfinder.service;

import com.hoang.jobfinder.dto.profile.request.HRProfileEditRequestDTO;
import com.hoang.jobfinder.dto.profile.response.HRProfileResponseDTO;

public interface HRProfileService {
  HRProfileResponseDTO findProfileByHRId(Long hrId);
  HRProfileResponseDTO editProfile(HRProfileEditRequestDTO editRequestDTO, Long hrId);
}
