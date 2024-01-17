package com.example.spacelab.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("application.teamwork")
@RequiredArgsConstructor
public class TeamworkProperties {
    private String apiVersion;
    private String baseUrl;
    private String projectId;
    private String token;
}
