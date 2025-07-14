package com.yc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 文件名: ${NAME}
 * 创建者: @一茶
 * 创建时间:2025/5/8 8:40
 * 描述：
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.yc","com.core"})
public class FileServiceApplication {
    public static void main(String[] args) {
       SpringApplication.run(FileServiceApplication.class,args);
    }
}
