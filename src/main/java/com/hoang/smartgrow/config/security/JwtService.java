package com.hoang.smartgrow.config.security;

import com.hoang.smartgrow.common.Role;
import com.hoang.smartgrow.dto.auth.UserTokenPayloadDTO;
import com.hoang.smartgrow.entity.User;
import com.hoang.smartgrow.property.AuthTokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
  private final AuthTokenProperties authTokenProperties;

  private SecretKey getSigningSecretKey() {
    return Keys.hmacShaKeyFor(authTokenProperties.getJwtSecret().getBytes());
  }

  public String generateToken(@NonNull User user) {
    Key secretKey = getSigningSecretKey();

    Instant currentTime = Instant.now();
    Instant expirationTime = currentTime.plusSeconds(authTokenProperties.getAccessTokenTTL() * 60);

    Map<String, Object> tokenClaims = new HashMap<>();
    tokenClaims.put("userId", user.getUserId());
    tokenClaims.put("username", user.getUsername());
    tokenClaims.put("fullName", user.getFullName());
    tokenClaims.put("role", user.getRole());

    return Jwts.builder()
        .expiration(Date.from(expirationTime))
        .subject(user.getUsername())
        .issuedAt(Date.from(currentTime))
        .claims(tokenClaims)
        .signWith(secretKey)
        .compact();
  }

  public Boolean validateToken(String token, String tokenType) {
    try {
      UserTokenPayloadDTO payload = getTokenPayload(token);
      return payload != null;
    } catch (Exception e) {
      log.warn("{} token invalid: {}", tokenType, e.getMessage());
      return false;
    }
  }

  public UserTokenPayloadDTO getTokenPayload(@NonNull String token) {
    String rawToken = token.replace("Bearer ", "").trim();

    Claims payload = Jwts.parser()
        .verifyWith(getSigningSecretKey())
        .build()
        .parseSignedClaims(rawToken)
        .getPayload();

    return UserTokenPayloadDTO.builder()
        .userId(payload.get("userId", Long.class))
        .username(payload.get("username", String.class))
        .fullName(payload.get("fullName", String.class))
        .role(Role.valueOf(payload.get("role", String.class)))
        .build();
  }
}