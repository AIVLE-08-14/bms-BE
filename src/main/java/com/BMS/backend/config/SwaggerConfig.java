package com.BMS.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                // 🔐 JWT 인증 설정 (Authorize 버튼 생성됨)
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .components(
                        new io.swagger.v3.oas.models.Components()
                                .addSecuritySchemes("JWT",
                                        new SecurityScheme()
                                                .name("Authorization")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )

                // 📘 API 기본 정보
                .info(apiInfo())

                // 🌍 서버 정보 (로컬 기준)
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Server")
                ));
    }

    private Info apiInfo() {
        return new Info()
                .title("BMS Backend API")
                .description("Books Management System Backend API 문서입니다.")
                .version("v1.0.0")
                .contact(
                        new Contact()
                                .name("BMS Backend Team")
                                .email("bms@example.com")
                );
    }
}
