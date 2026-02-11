package com.hoang.jobfinder.dto.cv;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CVResponseDTO {
  private Long cvId;
  private String cvKey;
  private String cvUrl;
  private Integer urlTTL;
}
