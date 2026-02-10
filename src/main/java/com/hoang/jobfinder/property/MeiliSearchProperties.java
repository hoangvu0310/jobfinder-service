package com.hoang.jobfinder.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "meilisearch")
public class MeiliSearchProperties {
  private String host;
  private String masterKey;
}
