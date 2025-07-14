package com.yc.payment.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * RabbitMQ 工具类：统一发送消息接口
 * @author yc
 *
 * 温馨提示：
 * 本地--延迟插件安装教程：
 *      延迟队列交换机插件（版本要兼容）：https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases
 *      （各种插件地址：https://www.rabbitmq.com/community-plugins?spm=5176.28103460.0.0.297c451e74b1kB）
 *      将下载的 .ez 文件复制到 RabbitMQ 的插件目录。插件目录通常位于
 *      /usr/lib/rabbitmq/lib/rabbitmq_server-<version>/plugins/。
 *      然后启用插件cmd(管理员)：rabbitmq-plugins enable rabbitmq_delayed_message_exchange
 *      验证：rabbitmq-plugins list
 *      （在 "Exchanges" 选项卡中，尝试创建一个新的交换机。如果插件安装成功，你应该能看到
 *      x-delayed-message 类型的交换机。）
 * 注：如果出现'rabbitmq-plugins' 不是内部或外部命令，也不是可运行的程序或批处理文件。
 *     教程：https://blog.csdn.net/weixin_43997764/article/details/104848466
 */
@Component
public class RabbitMqUtils {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;
//    private final RabbitTemplate rabbitTemplate;
//    private final ObjectMapper objectMapper;
//
//    @Autowired
//    public RabbitMqUtils(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
//        this.rabbitTemplate = rabbitTemplate;
//        this.objectMapper = objectMapper;
//    }

    /**
     * 发送字符串消息到指定队列
     *
     * @param queueName 队列名称
     * @param message   消息内容
     */
    public void send(String queueName, String message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }

    /**
     * 发送 JSON 格式对象消息到指定队列
     *
     * @param queueName 队列名称
     * @param payload   消息体（POJO）
     */
    public void sendJson(String queueName, Object payload) {
        try {
            //  将 POJO 转换为字节数组
            byte[] body = objectMapper.writeValueAsBytes(payload);
            // 设置消息类型为 JSON
            MessageProperties props = new MessageProperties();
            props.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            // 创建消息对象
            Message message = new Message(body, props);
            rabbitTemplate.send(queueName, message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize message", e);
        }
    }

    /**
     * 发送到指定交换机 + 路由键
     *
     * @param exchange    交换机名称
     * @param routingKey  路由键
     * @param payload     消息体
     */
    public void sendToExchange(String exchange, String routingKey, Object payload) {
        rabbitTemplate.convertAndSend(exchange, routingKey, payload);
    }

    /**
     * 发送延迟消息（需配合延迟插件或死信队列）
     *
     * @param queueName 队列名
     * @param message   消息体
     * @param delayMs   延迟时间（毫秒）
     */
    public void sendDelayed(String queueName, Object message, int delayMs) {
        try {
            byte[] body = objectMapper.writeValueAsBytes(message);
            MessageProperties props = new MessageProperties();
            props.setDelay(delayMs); // 注意：需要 RabbitMQ 安装延迟插件才支持
            props.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            Message msg = new Message(body, props);
            rabbitTemplate.send(queueName, msg);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send delayed message", e);
        }
    }

    /**
     * 发送带自定义头的消息
     *
     * @param queueName 队列名
     * @param message   消息体
     * @param headers   自定义头信息
     */
    public void sendWithHeaders(String queueName, Object message, Map<String, Object> headers) {
        try {
            byte[] body = objectMapper.writeValueAsBytes(message);
            MessageProperties props = new MessageProperties();
            props.getHeaders().putAll(headers);
            props.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            Message msg = new Message(body, props);
            rabbitTemplate.send(queueName, msg);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message with headers", e);
        }
    }
}
