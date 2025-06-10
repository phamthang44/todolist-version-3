package com.greenwich.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AppConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins(String.valueOf(List.of(
                    "http://localhost:3000",
                    "https://localhost:5173",
                    "https://localhost:5500"
                )))
                .allowedMethods("*")
                .allowedHeaders("*");
            }
        };
    }
}
