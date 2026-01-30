package com.hoang.jobfinder.dto.company.request;


import com.hoang.jobfinder.common.Enum;
import lombok.Data;

@Data
public class CompanyAssetRequestDTO {
  private Long companyAssetId;
  private String assetKey;
  private Enum.CompanyAssetType assetType;
  private String fileType;

}
