package com.hoang.jobfinder.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "auth-token")
public class AuthTokenProperties {
    private String jwtSecret;
    private Integer accessTokenTTL;
    private Integer refreshTokenTTL;
}
