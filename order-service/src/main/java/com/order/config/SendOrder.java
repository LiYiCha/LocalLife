package com.order.config;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件名: SendOrder
 * 创建者: @一茶
 * 创建时间:2025/2/11 19:39
 * 描述：
 */
@Slf4j
@Component
public class SendOrder {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public void configureMessageConverter(RabbitTemplate rabbitTemplate) {
        MessageConverter converter = new Jackson2JsonMessageConverter();
        rabbitTemplate.setMessageConverter(converter);
    }

    /*  在订单创建时发送消息到延迟队列
    order.delay.exchange是交换机名称
    order.delay.routing.key是路由键名称
    orderId是发送的订单ID
    */
    public void sendOrderToDelayQueue(Integer userId, Integer orderId) {
        try {
            Map<String, Integer> data = new HashMap<>();
            data.put("userId", userId);
            data.put("orderId", orderId);
            // 设置延迟时间为 30 分钟（单位：毫秒）
            rabbitTemplate.convertAndSend("order.delay.exchange", "order.delay.routing.key", data, message -> {
                message.getMessageProperties().setHeader("x-delay",  60 * 1000); // 30分钟延迟
                return message;
            });
            log.info("用户: {} 消息发送成功, 订单ID: {}", userId, orderId);
        } catch (Exception e) {
            log.error("用户: {} 消息发送失败, 订单ID: {}, 原因: {}", userId, orderId, e.getMessage());
        }
    }
}
