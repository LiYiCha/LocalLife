package com.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.feign.*")
@ComponentScan(basePackages = {"com.core.utils","com.auth.controller"})
@EnableWebSecurity  //  开启Spring Security
@EnableGlobalMethodSecurity(prePostEnabled = true)  //全局方法权限控制
public class LifeAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(LifeAuthApplication.class, args);
    }

}
