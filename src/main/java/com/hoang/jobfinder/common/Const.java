package com.hoang.jobfinder.common;

public class Const {

  public static final String API_PREFIX = "/api/v1";

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
  }
}
