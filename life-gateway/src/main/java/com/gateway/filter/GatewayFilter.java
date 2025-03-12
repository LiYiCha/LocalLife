
package com.gateway.filter;

import com.gateway.config.UrlWhiteConfig;
import com.core.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class GatewayFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(GatewayFilter.class);

    @Autowired
    private UrlWhiteConfig urlWhiteConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求路径
        String path = exchange.getRequest().getURI().getPath();
        logger.info("请求路径是: {}", path);
        // 检查路径是否在白名单中
        List<String> whiteList = urlWhiteConfig.getList();
        if (whiteList.stream().anyMatch(path::startsWith)) {
            // 如果在白名单中，直接放行
            return chain.filter(exchange);
        }

        // 获取请求头中的token
        String token = exchange.getRequest().getHeaders().getFirst("token");

        if (token == null || token.isEmpty()) {
            // 如果token缺失，返回401 Unauthorized
            logger.warn("Unauthorized access attempt to path: {} with missing token", path);
            return createErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "Token is missing");
        }

        // 验证token
        boolean verify = TokenUtil.verify(token);
        if (!verify) {
            // 如果token验证失败，返回401 Unauthorized
            logger.warn("Unauthorized access attempt to path: {} with invalid token: {}", path, token);
            return createErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "Invalid token");
        }
        // 如果token验证成功，放行
        return chain.filter(exchange);
    }

    private Mono<Void> createErrorResponse(ServerWebExchange exchange, HttpStatus status, String message) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String responseBody = String.format("{\"code\": 401, \"error\": \"Unauthorized\", \"message\": \"%s\"}", message);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(responseBody.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }


    @Override
    public int getOrder() {
        return -1; // 设置过滤器的优先级
    }
}
