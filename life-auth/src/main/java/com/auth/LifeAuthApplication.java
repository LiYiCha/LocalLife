package com.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.feign.*")
@ComponentScan(basePackages = {"com.core.utils","com.auth.controller"})
public class LifeAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(LifeAuthApplication.class, args);
    }

}
