package com.hoang.jobfinder.service.impl;

import com.hoang.jobfinder.common.ResultCode;
import com.hoang.jobfinder.dto.profile.request.HRProfileEditRequestDTO;
import com.hoang.jobfinder.dto.profile.response.HRProfileResponseDTO;
import com.hoang.jobfinder.entity.HRProfile;
import com.hoang.jobfinder.exception.JobFinderException;
import com.hoang.jobfinder.repository.HRProfileRepository;
import com.hoang.jobfinder.service.HRProfileService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class HRProfileServiceImpl implements HRProfileService {
  private HRProfileRepository hrProfileRepository;

  private ModelMapper modelMapper;

  @Override
  public HRProfileResponseDTO findProfileByHRId(Long hrId) {
    HRProfile profile = hrProfileRepository.findHRProfileByUserId(hrId);
    if (profile == null) {
      throw new JobFinderException(ResultCode.NOT_FOUND);
    }

    return modelMapper.map(profile, HRProfileResponseDTO.class);
  }

  @Override
  @Transactional
  public HRProfileResponseDTO editProfile(HRProfileEditRequestDTO editRequestDTO, Long hrId) {
    HRProfile hrProfile = hrProfileRepository.findHRProfileByUserId(hrId);
    if (hrProfile == null) {
      throw new JobFinderException(ResultCode.NOT_FOUND);
    }

    hrProfile.setDescription(editRequestDTO.getDescription());
    hrProfile.setTitle(editRequestDTO.getTitle());
    hrProfile.setFullName(editRequestDTO.getFullName());
    hrProfile.setPhoneNumber(editRequestDTO.getPhoneNumber());

    return modelMapper.map(hrProfile, HRProfileResponseDTO.class);
  }
}
