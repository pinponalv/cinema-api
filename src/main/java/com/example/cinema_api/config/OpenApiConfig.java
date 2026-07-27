package com.example.cinema_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "JWT Authorization header using the Bearer scheme."
)
public class OpenApiConfig {
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Cinema RESTful API")
                        .version("1.0")
                        .description("Cinema RESTful API" +
                                "Gestion completa de una plataforma de cine" +
                                "Permite administrar peliculas, usuarios, roles y permisos con auth mediante jwt" +
                                "Incluye control de acceso basado en roles")
                        .contact(new Contact()
                                .name("pinponalv")
                                .url("https://github.com/pinponalv")
                                .email("pinpondev@gmail.com")));
    }

}
