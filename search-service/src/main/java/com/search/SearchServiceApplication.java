package com.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 文件名: SearchServiceApplication
 * 创建者: @一茶
 * 创建时间:2025/4/29 18:09
 * 描述：
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.search", "com.core"})
public class SearchServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchServiceApplication.class, args);
    }
}
