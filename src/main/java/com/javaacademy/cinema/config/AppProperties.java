package com.javaacademy.cinema.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.admin-auth-header")
@Data
public class AppProperties {
    private String headerValue;
}
