package com.hoang.jobfinder.dto.profile.request;

import com.hoang.jobfinder.dto.profile.EducationInfoDTO;
import com.hoang.jobfinder.dto.profile.WorkExperienceDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserProfileEditRequestDTO {
  private String fullName;
  private String phoneNumber;
  private String address;
  private String description;
  private List<WorkExperienceDTO> workExperienceList;
  private List<EducationInfoDTO> educationInfoList;
}
