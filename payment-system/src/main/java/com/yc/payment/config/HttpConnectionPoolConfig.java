package com.yc.payment.config;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 公共HTTP连接池配置
@Configuration
public class HttpConnectionPoolConfig {

    @Bean
    public PoolingHttpClientConnectionManager poolingConnManager() {
        PoolingHttpClientConnectionManager poolingConnManager =
                new PoolingHttpClientConnectionManager();
        poolingConnManager.setMaxTotal(200); // 最大连接数
        poolingConnManager.setDefaultMaxPerRoute(100); // 每个路由最大连接数
        return poolingConnManager;
    }
}
