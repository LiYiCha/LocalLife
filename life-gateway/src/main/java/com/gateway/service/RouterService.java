package com.gateway.service;

import org.springframework.cloud.gateway.route.RouteDefinition;

/**
 * 路由配置服务
 */
public interface RouterService {
    /**
     * 更新路由配置
     *
     * @param routeDefinition
     */
    void update(RouteDefinition routeDefinition);
    /**
     * 添加路由配置
     *
     * @param routeDefinition
     */
    void add(RouteDefinition routeDefinition);
}
