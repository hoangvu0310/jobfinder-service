package com.hoang.jobfinder.service.impl;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.common.ErrorCode;
import com.hoang.jobfinder.dto.FileTypeDTO;
import com.hoang.jobfinder.dto.UploadUrlResponseDTO;
import com.hoang.jobfinder.dto.auth.response.AccountInfoDTO;
import com.hoang.jobfinder.dto.cv.CVResponseDTO;
import com.hoang.jobfinder.dto.file.RequestFileUploadUrlDTO;
import com.hoang.jobfinder.entity.user.CV;
import com.hoang.jobfinder.entity.user.User;
import com.hoang.jobfinder.exception.JobFinderException;
import com.hoang.jobfinder.repository.UserRepository;
import com.hoang.jobfinder.service.CVService;
import com.hoang.jobfinder.service.SupabaseS3Service;
import com.hoang.jobfinder.util.FileUtil;
import com.hoang.jobfinder.util.UserUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CVServiceImpl implements CVService {
  private UserRepository userRepository;

  private SupabaseS3Service supabaseS3Service;

  @Override
  public List<CVResponseDTO> getAllCV() {
    User user = userRepository.findUserById(UserUtil.getCurrentUser().getUserId());

    return user.getCvSet()
        .stream()
        .map(cv -> CVResponseDTO.builder()
            .cvId(cv.getCvId())
            .cvKey(cv.getCvKey())
            .cvUrl(supabaseS3Service.generateSignedGetUrl(cv.getCvKey()))
            .urlTTL(Const.PRESIGNED_URL_DURATION)
            .build()
        )
        .toList();
  }

  @Override
  public CVResponseDTO getById(Long cvId) {
    User user = userRepository.findUserById(UserUtil.getCurrentUser().getUserId());

    Set<CV> cvSet = user.getCvSet();
    CV cv = cvSet.stream()
        .filter(cv1 -> cv1.getCvId().equals(cvId))
        .findFirst()
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin CV"));

    return CVResponseDTO.builder()
        .cvId(cv.getCvId())
        .cvKey(cv.getCvKey())
        .cvUrl(supabaseS3Service.generateSignedGetUrl(cv.getCvKey()))
        .urlTTL(Const.PRESIGNED_URL_DURATION)
        .build();
  }

  @Override
  public UploadUrlResponseDTO generateCVUploadUrl(FileTypeDTO fileTypeDTO) {
    FileUtil.validatePDFFileType(fileTypeDTO.getFileType());

    AccountInfoDTO infoDTO = UserUtil.getCurrentUser();

    String key = Const.StorageBucketFolder.USER_CV + "/user-" + infoDTO.getUserId() + "-cv-" + UUID.randomUUID();
    RequestFileUploadUrlDTO fileUploadUrlDTO = RequestFileUploadUrlDTO.builder()
        .fileType(fileTypeDTO.getFileType())
        .fileKey(key)
        .isBucketPrivate(true)
        .build();

    return UploadUrlResponseDTO.builder()
        .uploadUrl(supabaseS3Service.generateSignedUploadUrl(fileUploadUrlDTO))
        .key(key)
        .expireDurationMinute(Const.PRESIGNED_URL_DURATION)
        .build();
  }

  @Override
  @Transactional
  public void saveNewCV(String cvKey) {
    User user = userRepository.findUserById(UserUtil.getCurrentUser().getUserId());

    Set<CV> cvSet = user.getCvSet();
    cvSet.add(CV.builder().cvKey(cvKey).build());
  }

  @Override
  @Transactional
  public void updateCV(Long cvId) {
    User user = userRepository.findUserById(UserUtil.getCurrentUser().getUserId());

    Set<CV> cvSet = user.getCvSet();
    CV cv = cvSet.stream()
        .filter(cv1 -> cv1.getCvId().equals(cvId))
        .findFirst()
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin CV"));

    cv.setUpdatedAt(Instant.now());
    cv.setUpdatedBy(user.getEmail());
  }

  @Override
  @Transactional
  public void deleteCV(Long cvId) {
    User user = userRepository.findUserById(UserUtil.getCurrentUser().getUserId());

    Set<CV> cvSet = user.getCvSet();
    CV cv = cvSet.stream()
        .filter(cv1 -> cv1.getCvId().equals(cvId))
        .findFirst()
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin CV"));

    cvSet.remove(cv);
    supabaseS3Service.deleteFile(cv.getCvKey(), false);
  }
}
