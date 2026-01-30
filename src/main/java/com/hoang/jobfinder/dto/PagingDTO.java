package com.hoang.jobfinder.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagingDTO {
  private Integer pageNumber;
  private Integer pageSize;
}
