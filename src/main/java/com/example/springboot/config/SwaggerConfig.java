package com.example.springboot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

   @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("My Spring Boot API")  // ✅ Title shown on Swagger UI
                .version("1.0.0")             // ✅ API version
                .description("API documentation with JWT authentication"))  // ✅ Helpful doc for consumers
            .components(new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)     // ✅ HTTP-based auth
                    .scheme("bearer")                   // ✅ Indicates Bearer token
                    .bearerFormat("JWT")))              // ✅ JWT format for Bearer
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth")); // ✅ Applies globally to all endpoints
    }


}
