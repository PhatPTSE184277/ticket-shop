package com.ticketShop.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ticketShopOpenAPI() {
        return new OpenAPI()
            .info(new Info()
            .title("ticketShop.com API")
            .version("1.0")
            .description(
                    "Flash Sale Ticket System - High Concurrency Ticket Booking"
            ))
            .components(
            new Components()
                    .addSecuritySchemes(
                            "bearerAuth",
                            new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")
                    )
    );
    }
}
