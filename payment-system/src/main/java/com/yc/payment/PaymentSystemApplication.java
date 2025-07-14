package com.yc.payment;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 文件名: PaymentSystemApplication
 * 创建者: @一茶
 * 创建时间:2025/5/21 18:07
 * 描述：
 */
@SpringBootApplication
@EnableDubbo
@ComponentScan(basePackages = {"com.api.payment", "com.yc.payment"})
public class PaymentSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentSystemApplication.class, args);
    }
}
