// src/main/java/com/example/project/config/WebConfig.java
package com.example.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://yramus.com")
            .allowedMethods("*")
            .allowedHeaders("*");
    }
}
