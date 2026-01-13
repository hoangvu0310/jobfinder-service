package com.hoang.smartgrow.config.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

  private JwtAuthenticationFilter jwtAuthenticationFilter;

  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  private CustomAccessDeniedHandler accessDeniedHandler;

  private static final String[] SWAGGER_WHITELIST = {
      "/api/v1/swagger-ui.html",
      "/api/v1/swagger-ui/**",
      "/api/v1/api-docs/**"
  };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
    return httpSecurity
        .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth -> auth
                .requestMatchers("/api/v1/auth/signIn", "/api/v1/auth/signUp", "/api/v1/auth/refresh").permitAll()
                .requestMatchers(SWAGGER_WHITELIST).permitAll()
                .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(accessDeniedHandler)
        )
        .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("*"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
