package com.hoang.jobfinder.common;

public class Enum {
  public enum Role {
    ADMIN,
    USER,
    GUEST,
    HR_ADMIN,
    HR
  }

  public enum Platform {
    ANDROID,
    IOS
  }

  public enum AuthType {
    EMAIL_AND_PASSWORD,
    GOOGLE
  }

  public enum CompanyAssetType {
    AVATAR,
    WORKSPACE_IMG
  }

  public enum CreateEditStatus {
    PENDING,
    APPROVED,
    REJECTED
  }

  public enum ActionType {
    CREATE,
    EDIT
  }
}
