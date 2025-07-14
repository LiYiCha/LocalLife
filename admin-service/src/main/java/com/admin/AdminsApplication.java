package com.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 文件名: AdminsSpringboot
 * 创建者: @一茶
 * 创建时间:2025/6/30 13:38
 * 描述：
 */
@SpringBootApplication
@MapperScan("com.admin.mapper")
public class AdminsApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminsApplication.class, args);
     }
}
