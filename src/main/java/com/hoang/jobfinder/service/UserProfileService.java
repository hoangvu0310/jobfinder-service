package com.hoang.jobfinder.service;

import com.hoang.jobfinder.dto.profile.request.UserProfileEditRequestDTO;
import com.hoang.jobfinder.dto.profile.response.UserProfileResponseDTO;

public interface UserProfileService {
  UserProfileResponseDTO findProfileByUserId();
  UserProfileResponseDTO editProfile(UserProfileEditRequestDTO editRequestDTO);
}
