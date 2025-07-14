package com.core.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 获取上传地址配置类
 */
@Component
@ConfigurationProperties(prefix = "reggie")
public class ReggieConfig {
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

