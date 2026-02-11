package com.hoang.jobfinder.util;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.common.ErrorCode;
import com.hoang.jobfinder.exception.JobFinderException;

import java.util.Arrays;

public class FileUtil {
  public static void validateImageFileType(String fileType) {
    if (Arrays.stream(Const.IMAGE_VALID_TYPE).noneMatch(validType -> validType.equals(fileType))) {
      throw new JobFinderException(ErrorCode.ILLEGAL_FILE_TYPE, "File không đúng định dạng ảnh jpeg, png, webp");
    }
  }

  public static void validatePDFFileType(String fileType) {
    if (!fileType.equals("application/pdf")) {
      throw new JobFinderException(ErrorCode.ILLEGAL_FILE_TYPE, "File không đúng định dạng ảnh jpeg, png, webp");
    }
  }
}
