package com.hoang.jobfinder.util;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.entity.company.Company;
import com.hoang.jobfinder.entity.company.CompanyAsset;
import com.hoang.jobfinder.service.SupabaseS3Service;

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
}
