
package com.gateway.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.gateway.config.UrlWhiteConfig;
import com.core.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
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
        List<String> whiteList = urlWhiteConfig.getList();
        String path = exchange.getRequest().getURI().getPath();

        // 白名单路径直接放行
        if (whiteList.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        // 获取 Token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return createErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "Missing or invalid token");
        }

        token = token.substring(7); // 去掉 Bearer 前缀

        // 验证 Token
        if (!TokenUtil.verify(token)) {
            return createErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        // 解析角色并添加到请求头（供下游服务使用）
        try {
            DecodedJWT jwt = TokenUtil.decode(token);
            String role = jwt.getClaim("role").asString();
            String username = jwt.getClaim("username").asString();

            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header("X-User-Name", username)
                    .header("X-User-Role", role)
                    .build();

            exchange = exchange.mutate().request(request).build();
        } catch (Exception e) {
            return createErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "Failed to parse token");
        }

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
