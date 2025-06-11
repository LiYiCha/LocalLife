package com.order.config;

import com.order.enums.OrderStatus;
import com.order.pojo.Orders;
import com.order.service.impl.OrdersServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 文件名: OrderMessageListener
 * 创建者: @一茶
 * 创建时间:2025/2/11 10:26
 * 描述：
 */

@Slf4j
@Component
public class OrderMessageListener {

    @Resource
    private OrdersServiceImpl ordersService;

    // 消费者接收消息并取消订单
    @RabbitListener(queues = "order.delay.queue")
    public void handleMessage(Map<String, Object> map) {
            // 获取订单ID并转换为 Integer 类型
            Integer orderId = (Integer) map.get("orderId");
            Integer userId = (Integer) map.get("userId");
            if (orderId == null) {
                log.error("订单ID为空，无法处理消息");
                return;
            }
            // 查询订单信息
            Orders order = ordersService.getById(orderId);
            if (order != null && OrderStatus.PENDING.getCode().equals(order.getStatus())) {
                ordersService.cancelOrder(userId, orderId);
                log.info("用户:"+userId+"订单取消成功: 订单ID=" + orderId);
            } else {
                log.error("用户:"+userId+"订单状态不符合取消条件: 订单ID=" + orderId);
            }
    }
}
