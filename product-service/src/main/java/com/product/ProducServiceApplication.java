package com.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.feign.client")
@ComponentScan(basePackages = {"com.product", "com.core","com.dataresource"})
@MapperScan("com.product.mapper")
public class ProducServiceApplication {
	static {
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");
	}
	public static void main(String[] args) {
		SpringApplication.run(ProducServiceApplication.class, args);
	}

}
