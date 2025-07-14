package com.core.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 * 该类用于定义RabbitMQ的交换机、队列以及绑定关系
 */
@Configuration("coreRabbitMQConfig")
public class RabbitMQConfig {

    // 交换机名称
    public static final String PRODUCT_EVENT_EXCHANGE = "lfProduct.event.exchange";

    // 队列名称
    public static final String PRODUCT_ES_SYNC_QUEUE = "lfProduct.es.sync.queue";

    // 路由键
    public static final String ROUTING_KEY_SYNC = "lfProduct.sync";

    /**
     * 创建产品事件交换机
     *
     * @return DirectExchange实例
     */
    @Bean
    public DirectExchange productEventExchange() {
        return new DirectExchange(PRODUCT_EVENT_EXCHANGE);
    }

    /**
     * 创建产品ES同步队列
     *
     * @return Queue实例
     */
    @Bean
    public Queue productEsSyncQueue() {
        return new Queue(PRODUCT_ES_SYNC_QUEUE);
    }

    /**
     * 将产品ES同步队列绑定到产品事件交换机
     *
     * @param productEsSyncQueue 产品ES同步队列
     * @param productEventExchange 产品事件交换机
     * @return Binding实例
     */
    @Bean
    public Binding bindingProductEsSync(Queue productEsSyncQueue, DirectExchange productEventExchange) {
        return BindingBuilder.bind(productEsSyncQueue)
                             .to(productEventExchange)
                             .with(ROUTING_KEY_SYNC);
    }
}
