package com.hoang.jobfinder.config;

import com.hoang.jobfinder.property.MeiliSearchProperties;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class MeiliSearchConfig {
  private MeiliSearchProperties meiliSearchProperties;

  @Bean
  public Client client() {
    return new Client(new Config(
        meiliSearchProperties.getHost(),
        meiliSearchProperties.getMasterKey()
    ));
  }
}
