package com.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author 一茶
 * @description: 异步配置
 * @date 2023/9/25 14:08
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    public Executor taskExecutor() {
        //创建线程池
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); //核心线程数
        executor.setMaxPoolSize(20);  //最大线程数
        executor.setQueueCapacity(50); //队列容量
        executor.setThreadNamePrefix("Async-Thread-"); //线程前缀名
        executor.initialize(); //初始化线程池
        return executor;
    }
}
