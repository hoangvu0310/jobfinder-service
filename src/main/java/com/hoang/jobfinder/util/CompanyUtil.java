package com.hoang.jobfinder.util;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.dto.company.response.CompanyAssetResponseDTO;
import com.hoang.jobfinder.dto.company.response.CompanyDTO;
import com.hoang.jobfinder.entity.company.Company;
import com.hoang.jobfinder.entity.company.CompanyAsset;
import com.hoang.jobfinder.service.SupabaseS3Service;
import org.modelmapper.ModelMapper;

public class CompanyUtil {
  public static String getAvatarUrl(Company company, SupabaseS3Service supabaseS3Service) {
    CompanyAsset avatar = company.getCompanyAssets()
        .stream()
        .filter(companyAsset -> companyAsset.getAssetType().equals(Enum.CompanyAssetType.AVATAR))
        .findFirst()
        .orElse(null);

    if (avatar == null) return null;

    return supabaseS3Service.generatePublicGetUrl(avatar.getAssetKey());
  }

  public static CompanyDTO companyMapper(Company company, ModelMapper modelMapper, SupabaseS3Service supabaseS3Service) {
    CompanyDTO dto = modelMapper.map(company, CompanyDTO.class);
    dto.setCompanyAssets(company.getCompanyAssets().stream().map(
            asset -> CompanyAssetResponseDTO.builder()
                .assetUrl(supabaseS3Service.generatePublicGetUrl(asset.getAssetKey()))
                .assetType(asset.getAssetType())
                .build()
        ).toList()
    );

    return dto;
  }
}
