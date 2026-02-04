package com.hoang.jobfinder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PagingDTO {
  private Integer pageNumber;
  private Integer pageSize;
}
