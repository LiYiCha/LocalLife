package com.product.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.core.config.RabbitMQConfig;

/**
 * RabbitMQ 消息生产者
 */
@Component
public class RabbitMQProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 发送产品同步事件消息到 RabbitMQ
     *
     * @param message 消息内容
     */
    public void sendProductSyncEvent(String message) {
        this.amqpTemplate.convertAndSend(
                RabbitMQConfig.PRODUCT_EVENT_EXCHANGE,   // 交换机名称
                RabbitMQConfig.ROUTING_KEY_SYNC,         // 路由键
                message                                  // 消息内容
        );
    }
}
