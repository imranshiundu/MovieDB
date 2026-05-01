package com.example.moviesapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI movieDbOpenApi() {
        return new OpenAPI()
            .info(new Info()
                .title("MovieDB API")
                .version("2.0.0")
                .description("A local-first movie database with CRUD, search, filtering, relationships, statistics, SQLite persistence, and a built-in dashboard.")
                .contact(new Contact().name("Imran Shiundu").url("https://imranisdev.top"))
                .license(new License().name("MIT License")))
            .addServersItem(new Server().url("http://localhost:8081").description("Local development server"));
    }
}
