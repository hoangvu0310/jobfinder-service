package com.hoang.jobfinder.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "supabase.s3")
public class SupabaseS3Properties {
  private String endpointUrl;
  private String region;
  private String accessKeyId;
  private String secretAccessKey;
  private String bucketName;
}
