package com.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 路由相关配置类
 */
@ConfigurationProperties(prefix = "gateway.routes.config")
@Component
@Data
public class GatewayRouteConfigProperties {
    private String dataId;
    private String group;

    private String namespace;
}
