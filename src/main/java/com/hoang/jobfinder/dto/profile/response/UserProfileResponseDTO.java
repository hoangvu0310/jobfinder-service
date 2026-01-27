package com.hoang.jobfinder.dto.profile.response;

import com.hoang.jobfinder.dto.profile.EducationInfoDTO;
import com.hoang.jobfinder.dto.profile.WorkExperienceDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserProfileResponseDTO {
  private String fullName;
  private String phoneNumber;
  private String email;
  private String address;
  private String description;
  private List<WorkExperienceDTO> workExperienceList;
  private List<EducationInfoDTO> educationInfoList;
}
