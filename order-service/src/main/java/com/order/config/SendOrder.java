package com.order.config;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 文件名: SendOrder
 * 创建者: @一茶
 * 创建时间:2025/2/11 19:39
 * 描述：
 */
@Component
public class SendOrder {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /*  在订单创建时发送消息到延迟队列
    order.delay.exchange是交换机名称
    order.delay.routing.key是路由键名称
    orderId是发送的订单ID
    */
    public void sendOrderToDelayQueue(Integer orderId) {
        rabbitTemplate.convertAndSend("order.delay.exchange", "order.delay.routing.key", orderId, message -> {
            message.getMessageProperties().getHeaders().put("x-delay", 60 * 1000); // 30分钟延迟
            return message;
        });
    }
}
