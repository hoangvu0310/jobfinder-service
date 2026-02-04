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

  public enum JobStatus {
    OPEN,
    CLOSE
  }

  public enum ExperienceLevel {
    INTERN,
    FRESHER,
    JUNIOR,
    MIDDLE,
    SENIOR,
    LEADER
  }

  public enum JobType {
    PART_TIME,
    FULL_TIME,
    INTERN,
    PROJECT_BASED
  }

  public enum WorkplaceType {
    REMOTE,
    HYBRID,
    ONSITE
  }

  public enum ActionType {
    CREATE,
    EDIT
  }
}
