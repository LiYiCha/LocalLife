package com.order.config;

import com.order.enums.OrderStatus;
import com.order.pojo.Orders;
import com.order.service.impl.OrdersServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 文件名: OrderMessageListener
 * 创建者: @一茶
 * 创建时间:2025/2/11 10:26
 * 描述：
 */


@Component
public class OrderMessageListener {

    @Resource
    private OrdersServiceImpl ordersService;

    // 消费者接收消息并取消订单
    @RabbitListener(queues = "order.delay.queue")
    public void handleOrderDelay(Integer userId,Integer orderId) {
        Orders order = ordersService.getById(orderId);
        if (order != null && OrderStatus.PENDING.getCode().equals(order.getStatus())) {
            ordersService.cancelOrder(userId,orderId);
        }
    }
}
