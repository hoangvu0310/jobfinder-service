package com.hoang.jobfinder.service.impl;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.common.ErrorCode;
import com.hoang.jobfinder.dto.auth.response.AccountInfoDTO;
import com.hoang.jobfinder.dto.company.request.CompanyInfoPostRequestDTO;
import com.hoang.jobfinder.dto.company.response.CompanyAssetResponseDTO;
import com.hoang.jobfinder.dto.company.response.CompanyDTO;
import com.hoang.jobfinder.dto.company.response.CompanyDraftDTO;
import com.hoang.jobfinder.entity.HR;
import com.hoang.jobfinder.entity.company.Company;
import com.hoang.jobfinder.entity.company.CompanyDraft;
import com.hoang.jobfinder.exception.JobFinderException;
import com.hoang.jobfinder.repository.CompanyDraftRepository;
import com.hoang.jobfinder.repository.CompanyRepository;
import com.hoang.jobfinder.repository.HRRepository;
import com.hoang.jobfinder.service.CompanyService;
import com.hoang.jobfinder.service.SupabaseS3Service;
import com.hoang.jobfinder.util.UserUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CompanyServiceImpl implements CompanyService {
  private CompanyRepository companyRepository;

  private CompanyDraftRepository companyDraftRepository;

  private HRRepository hrRepository;

  private SupabaseS3Service supabaseS3Service;

  private ModelMapper modelMapper;

  private ObjectMapper objectMapper;

  @Override
  @Transactional
  public CompanyDraftDTO createCompany(CompanyInfoPostRequestDTO requestDTO) {
    if (companyRepository.existsCompanyByCompanyName(requestDTO.getCompanyName())) {
      throw new JobFinderException(ErrorCode.BAD_REQUEST, "Tên công ty đã tồn tại");
    }

    AccountInfoDTO hrInfo = UserUtil.getCurrentUser();
    CompanyDraft draft = CompanyDraft.builder()
        .payload(objectMapper.writeValueAsString(requestDTO))
        .status(Enum.CreateEditStatus.PENDING)
        .actionType(Enum.ActionType.CREATE)
        .postedBy(hrInfo.getEmail())
        .postedAt(Instant.now())
        .build();

    companyDraftRepository.save(draft);

    CompanyDraftDTO dto = modelMapper.map(draft, CompanyDraftDTO.class);
    dto.setPayload(requestDTO);

    return dto;
  }

  @Override
  @Transactional
  public CompanyDraftDTO editCompanyInfo(CompanyInfoPostRequestDTO requestDTO, Long companyId) {
    Company company = companyRepository.findCompanyByCompanyId(companyId).get(0);
    AccountInfoDTO hrInfo = UserUtil.getCurrentUser();
    HR hr = hrRepository.findHRById(hrInfo.getUserId());

    if (!company.getHrList().contains(hr) && !hrInfo.getRole().equals(Enum.Role.HR_ADMIN)) {
      throw new JobFinderException(ErrorCode.FORBIDDEN, "Tài khoản không có quyền chỉnh sửa", HttpStatus.FORBIDDEN);
    }

    CompanyDraft draft = CompanyDraft.builder()
        .companyId(companyId)
        .payload(objectMapper.writeValueAsString(requestDTO))
        .status(Enum.CreateEditStatus.PENDING)
        .actionType(Enum.ActionType.EDIT)
        .postedBy(hr.getEmail())
        .postedAt(Instant.now())
        .build();

    companyDraftRepository.save(draft);

    CompanyDraftDTO dto = modelMapper.map(draft, CompanyDraftDTO.class);
    dto.setPayload(requestDTO);

    return dto;
  }

  @Override
  public CompanyDTO getCompanyInfo(Long companyId) {
    Company company = companyRepository.findCompanyByCompanyId(companyId).get(0);

    return companyMapper(company);
  }

  private CompanyDTO companyMapper(Company company) {
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
