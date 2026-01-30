package com.hoang.jobfinder.dto.company.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class CompanyDTO {
  private String companyName;
  private String address;
  private Double latitude;
  private Double longitude;
  private String description;
  private String websiteUrl;
  private Integer companySize;
  private List<CompanyAssetResponseDTO> companyAssets;
  private Set<DescriptionTagDTO> tags;
}
