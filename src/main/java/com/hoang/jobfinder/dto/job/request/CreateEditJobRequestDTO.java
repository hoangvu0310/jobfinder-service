package com.hoang.jobfinder.dto.job.request;

import com.hoang.jobfinder.common.Enum;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateEditJobRequestDTO {
  @NotBlank(message = "Mô tả yêu cầu tạo/sửa công việc không được để trống")
  private String requestDescription;

  @NotBlank(message = "Tên công việc không được để trống")
  private String jobTitle;

  private String city;

  private Integer minSalary;

  private Integer maxSalary;

  @NotBlank(message = "Mô tả công việc không được để trống")
  private String description;

  @NotBlank(message = "Yêu cầu công việc không được để trống")
  private String requirement;

  @NotBlank(message = "Quyền lợi không được để trống")
  private String benefit;

  private Integer employeeNeed;

  private String workAddress;

  private LocalDate dueDate;

  private Enum.JobType jobType;

  private Enum.ExperienceLevel experienceLevel;

  private Enum.WorkplaceType workplaceType;
}
