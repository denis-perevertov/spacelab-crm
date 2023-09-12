package com.example.spacelab.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                    .bearerFormat("JWT")
                                    .scheme("bearer")
                                    .description("Use JWT access token from \"login\" endpoint")
                                    .name("JWT Authorization");
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                            .components(new Components()
                                            .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
                            .info(new Info()
                                            .title("Spacelab LMS API")
                                            .description("API for Spacelab LMS - working w/ students, content management, conducting lessons etc")
                                            .version("1.0")
                                            .contact(new Contact()
                                                                .name("Denis Perevertov & Evgeniy Shevchenko")
                                                                .email("testemail@gmail.com")
                                                                .url("http://www.test.com"))
                                            .license(new License()
                                                                .name("License of API")
                                                                .url("API license URL")));
    }
}
