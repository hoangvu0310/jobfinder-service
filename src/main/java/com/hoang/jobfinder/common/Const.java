package com.hoang.jobfinder.common;

import org.springframework.util.MimeTypeUtils;

public class Const {

  public static final String API_PREFIX = "/api/v1";
  public static final String HR_API_PREFIX = "/api/v1/hr";
  public static final String ADMIN_API_PREFIX = "/api/v1/admin";


  public static final Integer PRESIGNED_URL_DURATION = 30;  // minutes

  public static final String[] IMAGE_VALID_TYPE = {
      MimeTypeUtils.IMAGE_JPEG_VALUE,
      MimeTypeUtils.IMAGE_PNG_VALUE,
      "image/webp"
  };

  public interface Regex {
    // Password: contain at least 1 upper case, 1 lower case, 1 number, no whitespace, min 8 letters
    String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    String EMAIL = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
  }

  public interface TokenType {
    String ACCESS = "Access";
    String REFRESH = "Access";
  }

  public interface StorageBucketFolder {
    String COMPANY_AVATAR = "company-avatar";
    String COMPANY_ASSET = "company-asset";
    String USER_AVATAR = "user-avatar";
    String USER_CV = "user-cv";
    String HR_AVATAR = "hr-avatar";
  }

  public interface SearchIndexName {
    String JOBS = "jobs";
  }
}
