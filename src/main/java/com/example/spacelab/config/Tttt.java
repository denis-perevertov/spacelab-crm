package com.example.spacelab.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("springdoc.swagger-ui")
@RequiredArgsConstructor
public class Tttt {
    private String path;
    private String useRootPath;
    private String filter;
}
