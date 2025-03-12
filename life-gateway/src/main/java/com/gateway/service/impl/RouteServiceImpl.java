package com.gateway.service.impl;

import com.gateway.service.RouterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
/**
 * 路由配置服务实现类
 */
@Service
@Slf4j
public class RouteServiceImpl implements RouterService, ApplicationEventPublisherAware {
    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;
    /**
     * 事件发布者
     */
    private ApplicationEventPublisher publisher;
    @Override
    public void update(RouteDefinition routeDefinition) {
        log.info("更新路由配置项：{}", routeDefinition);
        this.routeDefinitionWriter.delete(Mono.just(routeDefinition.getId()));
        routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }
    @Override
    public void add(RouteDefinition routeDefinition) {
        log.info("新增路由配置项：{}", routeDefinition);
        routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}
