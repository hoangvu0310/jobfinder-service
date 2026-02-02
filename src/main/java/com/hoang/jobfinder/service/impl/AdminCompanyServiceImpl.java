package com.hoang.jobfinder.service.impl;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.common.ErrorCode;
import com.hoang.jobfinder.dto.PagingDTO;
import com.hoang.jobfinder.dto.auth.response.AccountInfoDTO;
import com.hoang.jobfinder.dto.company.request.CompanyInfoPostRequestDTO;
import com.hoang.jobfinder.dto.company.request.RejectRequestDTO;
import com.hoang.jobfinder.dto.company.response.CompanyAssetResponseDTO;
import com.hoang.jobfinder.dto.company.response.CompanyDTO;
import com.hoang.jobfinder.dto.company.response.CompanyDraftDTO;
import com.hoang.jobfinder.dto.company.response.DescriptionTagDTO;
import com.hoang.jobfinder.entity.company.Company;
import com.hoang.jobfinder.entity.company.CompanyAsset;
import com.hoang.jobfinder.entity.company.CompanyDraft;
import com.hoang.jobfinder.entity.company.DescriptionTag;
import com.hoang.jobfinder.exception.JobFinderException;
import com.hoang.jobfinder.repository.CompanyDraftRepository;
import com.hoang.jobfinder.repository.CompanyRepository;
import com.hoang.jobfinder.repository.DescriptionTagRepository;
import com.hoang.jobfinder.repository.HRRepository;
import com.hoang.jobfinder.service.AdminCompanyService;
import com.hoang.jobfinder.service.SupabaseS3Service;
import com.hoang.jobfinder.specification.CompanyDraftSpecification;
import com.hoang.jobfinder.util.FileUtil;
import com.hoang.jobfinder.util.UserUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AdminCompanyServiceImpl implements AdminCompanyService {
  private CompanyRepository companyRepository;

  private CompanyDraftRepository companyDraftRepository;

  private DescriptionTagRepository descriptionTagRepository;

  private HRRepository hrRepository;

  private ModelMapper modelMapper;

  private ObjectMapper objectMapper;

  private SupabaseS3Service supabaseS3Service;

  @Override
  public Page<CompanyDTO> getAllCompany(PagingDTO pagingDTO) {
    Pageable pageable = PageRequest.of(pagingDTO.getPageNumber(), pagingDTO.getPageSize());
    Page<Company> companyPage = companyRepository.findAll(pageable);

    return companyPage.map(company -> modelMapper.map(company, CompanyDTO.class));
  }

  @Override
  public Page<CompanyDraftDTO> getCompanyRequestList(PagingDTO pagingDTO, Enum.ActionType actionType) {
    Pageable pageable = PageRequest.of(pagingDTO.getPageNumber(), pagingDTO.getPageSize());
    Specification<CompanyDraft> draftSpecification = Specification.where(CompanyDraftSpecification.hasActionType(actionType));
    Page<CompanyDraft> companyDraftPage = companyDraftRepository.findAll(draftSpecification, pageable);

    return companyDraftPage.map(companyDraft -> {
      CompanyDraftDTO draftDTO = modelMapper.map(companyDraft, CompanyDraftDTO.class);
      draftDTO.setPayload(objectMapper.convertValue(companyDraft.getPayload(), CompanyInfoPostRequestDTO.class));
      return draftDTO;
    });
  }

  @Override
  public CompanyDTO getCompanyRequestById(Long draftId) {
    CompanyDraft draft = companyDraftRepository.findCompanyDraftByDraftId(draftId);

    CompanyInfoPostRequestDTO postRequestDTO = objectMapper.convertValue(draft.getPayload(), CompanyInfoPostRequestDTO.class);

    List<CompanyAssetResponseDTO> assetResponseDTOs = postRequestDTO.getCompanyAssets().stream().map(
        companyAssetDTO -> CompanyAssetResponseDTO.builder()
            .assetUrl(supabaseS3Service.generatePublicGetUrl(companyAssetDTO.getAssetKey()))
            .assetType(companyAssetDTO.getAssetType())
            .build()
    ).toList();

    Set<DescriptionTagDTO> descriptionTagDTOs = descriptionTagRepository.findAllById(postRequestDTO.getTagIds())
        .stream()
        .map(
            descriptionTag -> DescriptionTagDTO.builder()
                .title(descriptionTag.getTitle())
                .slug(descriptionTag.getSlug())
                .build()
        )
        .collect(Collectors.toSet());

    return CompanyDTO.builder()
        .companyName(postRequestDTO.getCompanyName())
        .address(postRequestDTO.getAddress())
        .latitude(postRequestDTO.getLatitude())
        .longitude(postRequestDTO.getLongitude())
        .description(postRequestDTO.getDescription())
        .websiteUrl(postRequestDTO.getWebsiteUrl())
        .companySize(postRequestDTO.getCompanySize())
        .companyAssets(assetResponseDTOs)
        .tags(descriptionTagDTOs)
        .build();
  }

  @Override
  @Transactional
  public CompanyDTO approveCreateRequest(Long draftId) {
    CompanyDraft draft = companyDraftRepository.findCompanyDraftByDraftId(draftId);
    AccountInfoDTO adminInfo = UserUtil.getCurrentUser();
    draft.setStatus(Enum.CreateEditStatus.APPROVED);
    draft.setHandledBy(adminInfo.getEmail());
    draft.setHandledAt(Instant.now());

    CompanyInfoPostRequestDTO postRequestDTO = objectMapper.convertValue(draft.getPayload(), CompanyInfoPostRequestDTO.class);

    Company newCompany = Company.builder()
        .companyName(postRequestDTO.getCompanyName())
        .address(postRequestDTO.getAddress())
        .latitude(postRequestDTO.getLatitude())
        .longitude(postRequestDTO.getLongitude())
        .description(postRequestDTO.getDescription())
        .websiteUrl(postRequestDTO.getWebsiteUrl())
        .companySize(postRequestDTO.getCompanySize())
        .build();

    AccountInfoDTO hrInfo = UserUtil.getCurrentUser();
    Set<DescriptionTag> tagList = new HashSet<>(descriptionTagRepository.findAllById(postRequestDTO.getTagIds()));
    List<CompanyAsset> assetList = postRequestDTO.getCompanyAssets()
        .stream()
        .map(asset -> {
              FileUtil.validateImageFileType(asset.getFileType());

              return CompanyAsset.builder()
                  .assetKey(asset.getAssetKey())
                  .assetType(asset.getAssetType())
                  .company(newCompany)
                  .build();
            }
        )
        .toList();


    newCompany.getTags().addAll(tagList);
    newCompany.getHrList().add(hrRepository.findHRById(hrInfo.getUserId()));
    newCompany.getCompanyAssets().addAll(assetList);

    companyRepository.save(newCompany);

    return companyMapper(newCompany);
  }

  @Override
  @Transactional
  public CompanyDraftDTO rejectRequest(RejectRequestDTO rejectRequestDTO) {
    AccountInfoDTO adminInfo = UserUtil.getCurrentUser();

    CompanyDraft draft = companyDraftRepository.findCompanyDraftByDraftId(rejectRequestDTO.getDraftId());
    draft.setStatus(Enum.CreateEditStatus.REJECTED);
    draft.setRejectReason(rejectRequestDTO.getRejectReason());
    draft.setHandledBy(adminInfo.getEmail());
    draft.setHandledAt(Instant.now());

    return null;
  }

  @Override
  public CompanyDTO approveEditRequest(Long draftId) {
    CompanyDraft draft = companyDraftRepository.findCompanyDraftByDraftId(draftId);
    AccountInfoDTO adminInfo = UserUtil.getCurrentUser();
    draft.setStatus(Enum.CreateEditStatus.APPROVED);
    draft.setHandledBy(adminInfo.getEmail());
    draft.setHandledAt(Instant.now());

    CompanyInfoPostRequestDTO postRequestDTO = objectMapper.convertValue(draft.getPayload(), CompanyInfoPostRequestDTO.class);

    Company company = companyRepository.findById(draft.getCompanyId())
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy dữ liệu của công ty"));

    company.setCompanyName(postRequestDTO.getCompanyName());
    company.setAddress(postRequestDTO.getAddress());
    company.setCompanySize(postRequestDTO.getCompanySize());
    company.setDescription(postRequestDTO.getDescription());
    company.setLatitude(postRequestDTO.getLatitude());
    company.setLongitude(postRequestDTO.getLongitude());
    company.setWebsiteUrl(postRequestDTO.getWebsiteUrl());

    Set<DescriptionTag> tags = company.getTags();
    tags.clear();
    tags.addAll(descriptionTagRepository.findAllById(postRequestDTO.getTagIds()));

    List<CompanyAsset> assets = company.getCompanyAssets();
    Map<Long, CompanyAsset> assetMap = assets.stream()
        .collect(Collectors.toMap(CompanyAsset::getCompanyAssetId, companyAsset -> companyAsset));
    assets.clear();

    postRequestDTO.getCompanyAssets().forEach(
        companyAssetDTO -> {
          CompanyAsset asset = companyAssetDTO.getCompanyAssetId() != null
              ? Optional.ofNullable(assetMap.get(companyAssetDTO.getCompanyAssetId()))
              .orElse(new CompanyAsset())
              : new CompanyAsset();

          asset.setAssetKey(companyAssetDTO.getAssetKey());
          asset.setAssetType(companyAssetDTO.getAssetType());
          asset.setCompany(company);
          asset.setFileType(companyAssetDTO.getFileType());
        }
    );

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
