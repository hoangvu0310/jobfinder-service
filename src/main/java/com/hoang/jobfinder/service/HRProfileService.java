package com.hoang.jobfinder.service;

import com.hoang.jobfinder.dto.profile.request.HRProfileEditRequestDTO;
import com.hoang.jobfinder.dto.profile.response.HRProfileResponseDTO;

public interface HRProfileService {
  HRProfileResponseDTO findProfileByHRId();
  HRProfileResponseDTO editProfile(HRProfileEditRequestDTO editRequestDTO);
}
