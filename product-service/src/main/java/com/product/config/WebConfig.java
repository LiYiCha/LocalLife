package com.product.config;

import com.core.config.ReggieConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ReggieConfig reggieConfig;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将 /files/** 映射到物理路径
        registry.addResourceHandler("/files/**")
                .addResourceLocations("files:"+ reggieConfig.getPath());
    }

}
