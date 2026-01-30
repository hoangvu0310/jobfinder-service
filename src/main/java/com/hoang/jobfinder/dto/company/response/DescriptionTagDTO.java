package com.hoang.jobfinder.dto.company.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DescriptionTagDTO {
  private String title;
  private String slug;
}
