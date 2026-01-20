package com.hoang.jobfinder.config;

import com.hoang.jobfinder.property.SupabaseS3Properties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
@AllArgsConstructor
public class SupabaseS3Config {
  private SupabaseS3Properties supabaseS3Properties;

  @Bean
  public S3Client createS3Client() {
    return S3Client.builder()
        .endpointOverride(URI.create(supabaseS3Properties.getEndpointUrl()))
        .region(Region.of(supabaseS3Properties.getRegion()))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    supabaseS3Properties.getAccessKeyId(),
                    supabaseS3Properties.getSecretAccessKey()
                )
            )
        )
        .serviceConfiguration(
            S3Configuration.builder()
              .pathStyleAccessEnabled(true)
              .build())
        .build();
  }

  @Bean
  public String supabaseBucketName() {
    return supabaseS3Properties.getBucketName();
  }

  @Bean
  public S3Presigner s3Presigner() {
    return S3Presigner.builder()
        .endpointOverride(URI.create(supabaseS3Properties.getEndpointUrl()))
        .region(Region.of(supabaseS3Properties.getRegion()))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    supabaseS3Properties.getAccessKeyId(),
                    supabaseS3Properties.getSecretAccessKey()
                )
            )
        )
        .serviceConfiguration(
            S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build())
        .build();
  }
}
