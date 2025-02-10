package com.javaacademy.cinema.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = AppProperties.class)
@RequiredArgsConstructor
public class AppConfig {
    private final AppProperties appProperties;

    @Bean
    public AppProperties appProperties() {
        return appProperties;
    }
}
