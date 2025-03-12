package com.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.feign.client")
@ComponentScan(basePackages = {"com.product", "com.core"})
@MapperScan("com.product.mapper")
public class ProducServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProducServiceApplication.class, args);
	}

}
