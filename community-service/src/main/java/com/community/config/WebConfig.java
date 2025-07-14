package com.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将 /files/** 映射到物理路径
        registry.addResourceHandler("/files/**")
                .addResourceLocations("files:D:/exploitation/work1/LocalLife/community-service/src/main/resources/static/files/");
    }

}
