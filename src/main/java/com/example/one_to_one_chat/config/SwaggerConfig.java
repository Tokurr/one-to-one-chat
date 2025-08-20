package com.example.one_to_one_chat.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("One-to-One Chat API")
                        .description("Mesajlaşma sistemi için REST API dokümantasyonu")
                        .version("1.0.0"));
    }
}
