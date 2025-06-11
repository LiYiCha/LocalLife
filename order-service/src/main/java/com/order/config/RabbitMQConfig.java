package com.order.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration("orderRabbitMQConfig")
public class RabbitMQConfig {

    // 定义延迟交换机
    @Bean
    public CustomExchange orderDelayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct"); // 设置延迟交换机的类型
        return new CustomExchange("order.delay.exchange", "x-delayed-message", true, false, args);
    }

    // 定义延迟队列
    @Bean
    public Queue orderDelayQueue() {
        return QueueBuilder.durable("order.delay.queue")
                .withArgument("x-dead-letter-exchange", "order.delay.exchange")
                .withArgument("x-dead-letter-routing-key", "order.delay.routing.key")
                .build();
    }

    // 绑定延迟队列和延迟交换机
    @Bean
    public Binding orderDelayBinding() {
        return BindingBuilder.bind(orderDelayQueue())
                .to(orderDelayExchange())
                .with("order.delay.routing.key")
                .noargs();
    }

    // 消息转换器
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
