package com.example.springX.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application.properties")
@SecurityScheme(
        name="basicAuth",
        type= SecuritySchemeType.HTTP,
        scheme = "basic")
@Configuration
public class SwaggerConfiguration {

    @Bean
    public GroupedOpenApi MaybankAPI() {
        return GroupedOpenApi.builder()
                .group("MAYBANK API V1")
                .pathsToMatch("/api/v1/**")
                .build();
    }
}
