package com.yc.payment.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    /**
     * 创建 ObjectMapper 对象，用于序列化和反序列化 JSON 数据
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * 创建 MessageConverter 对象，用于将消息转换为 JSON 格式
     */
    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }

    /**
     * 配置 RabbitTemplate 对象，将 MessageConverter 设置为 JSON 格式
     */
//    @Bean
//    public RabbitTemplate configureRabbitTemplate(RabbitTemplate rabbitTemplate, MessageConverter converter) {
//        rabbitTemplate.setMessageConverter(converter);
//        return rabbitTemplate;
//    }
    @Autowired
    public void configureRabbitTemplate(RabbitTemplate rabbitTemplate, MessageConverter converter) {
        rabbitTemplate.setMessageConverter(converter);
    }


    /**
     * 创建订单支付成功队列
     */
    @Bean
    public Queue orderPaymentSuccessQueue() {
        return new Queue("order_payment_success_queue");
    }
}

