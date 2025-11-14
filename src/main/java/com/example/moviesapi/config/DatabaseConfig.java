package com.example.moviesapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
public class DatabaseConfig {

    /**
     * Provides an AuditorAware bean for JPA auditing.
     * This will automatically set the auditor (createdBy, modifiedBy) for entities.
     * 
     * @return AuditorAware implementation that returns "movies-api-system" as the current auditor
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor() {
               
                return Optional.of("movies-api-system");
            }
        };
    }

    /*
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("movies-api-system");
    }
    */
}