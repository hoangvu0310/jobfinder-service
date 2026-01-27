package com.hoang.jobfinder.service.impl;

import com.hoang.jobfinder.common.ErrorCode;
import com.hoang.jobfinder.common.ResultCode;
import com.hoang.jobfinder.dto.auth.response.AccountInfoDTO;
import com.hoang.jobfinder.dto.profile.request.UserProfileEditRequestDTO;
import com.hoang.jobfinder.dto.profile.response.UserProfileResponseDTO;
import com.hoang.jobfinder.entity.user.EducationInfo;
import com.hoang.jobfinder.entity.user.UserProfile;
import com.hoang.jobfinder.entity.user.WorkExperience;
import com.hoang.jobfinder.exception.JobFinderException;
import com.hoang.jobfinder.repository.UserProfileRepository;
import com.hoang.jobfinder.service.UserProfileService;
import com.hoang.jobfinder.util.UserUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserProfileServiceImpl implements UserProfileService {
  private UserProfileRepository userProfileRepository;

  private ModelMapper modelMapper;

  @Override
  public UserProfileResponseDTO findProfileByUserId() throws JobFinderException {
    AccountInfoDTO info = UserUtil.getCurrentUser();

    UserProfile userProfile = userProfileRepository.findUserProfileByUserId(info.getUserId());
    if (userProfile == null) {
      throw new JobFinderException(ResultCode.NOT_FOUND);
    }

    return modelMapper.map(userProfile, UserProfileResponseDTO.class);
  }

  @Override
  @Transactional
  public UserProfileResponseDTO editProfile(UserProfileEditRequestDTO editRequestDTO) {
    AccountInfoDTO info = UserUtil.getCurrentUser();

    UserProfile userProfile = userProfileRepository.findUserProfileByUserId(info.getUserId());
    if (userProfile == null) {
      throw new JobFinderException(ResultCode.NOT_FOUND);
    }

    userProfile.setFullName(editRequestDTO.getFullName());
    userProfile.setPhoneNumber(editRequestDTO.getPhoneNumber());
    userProfile.setAddress(editRequestDTO.getAddress());
    userProfile.setDescription(editRequestDTO.getDescription());

    List<EducationInfo> educationInfos = userProfile.getEducationInfoList();
    List<WorkExperience> workExperiences = userProfile.getWorkExperienceList();

    Map<Long, EducationInfo> educationInfoMap = educationInfos.stream()
        .collect(Collectors.toMap(EducationInfo::getId, educationInfo -> educationInfo));
    Map<Long, WorkExperience> workExperienceMap = workExperiences.stream()
        .collect(Collectors.toMap(WorkExperience::getId, workExperience -> workExperience));

    educationInfos.clear();
    workExperiences.clear();

    editRequestDTO.getEducationInfoList().forEach(
        educationInfoDTO -> {
          EducationInfo educationInfo = educationInfoDTO.getId() != null
              ? Optional.ofNullable(educationInfoMap.get(educationInfoDTO.getId()))
                  .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin học vấn với id được gửi"))
              : new EducationInfo();

          educationInfo.setMajor(educationInfoDTO.getMajor());
          educationInfo.setFacility(educationInfoDTO.getFacility());
          educationInfo.setStartTime(educationInfoDTO.getStartTime());
          educationInfo.setEndTime(educationInfoDTO.getEndTime());
          educationInfo.setPositionInList(educationInfoDTO.getPositionInList());
          educationInfo.setUserProfile(userProfile);
        }
    );

    editRequestDTO.getWorkExperienceList().forEach(
        workExperienceDTO -> {
          WorkExperience workExperience = workExperienceDTO.getId() != null
              ? Optional.ofNullable(workExperienceMap.get(workExperienceDTO.getId()))
                  .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin kinh nghiệm làm việc với id được gửi"))
              : new WorkExperience();

          workExperience.setCompany(workExperienceDTO.getCompany());
          workExperience.setJobTitle(workExperienceDTO.getJobTitle());
          workExperience.setDescription(workExperienceDTO.getDescription());
          workExperience.setStartTime(workExperienceDTO.getStartTime());
          workExperience.setEndTime(workExperienceDTO.getEndTime());
          workExperience.setPositionInList(workExperienceDTO.getPositionInList());
          workExperience.setUserProfile(userProfile);
        }
    );

    return modelMapper.map(userProfile, UserProfileResponseDTO.class);
  }
}
