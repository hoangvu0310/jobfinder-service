package com.hoang.jobfinder.dto.company.request;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CompanyInfoPostRequestDTO {
  private String companyName;
  private String address;
  private Double latitude;
  private Double longitude;
  private String description;
  private String websiteUrl;
  private Integer companySize;
  private List<CompanyAssetRequestDTO> companyAssets;
  private Set<Long> tagIds;
}
