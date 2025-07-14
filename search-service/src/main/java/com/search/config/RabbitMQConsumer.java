package com.search.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.search.service.impl.EsSyncService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.core.config.RabbitMQConfig;

/**
 * RabbitMQ 消息消费者
 */
@Slf4j
@Component
public class RabbitMQConsumer {

    @Resource
    private EsSyncService esSyncService;

    /**
     * 监听 lfProduct.es.sync.queue 队列，并处理产品同步事件
     *
     * @param message 接收到的消息（格式如：{
     *   "type": "product",
     *   "id": 1001,
     *   "action": "create/update/delete"
     * }）
     */
    @RabbitListener(queues = RabbitMQConfig.PRODUCT_ES_SYNC_QUEUE)
    public void handleProductSyncEvent(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(message);
            String action = jsonNode.get("action").asText();
            Integer id = jsonNode.get("id").asInt();

            switch (action) {
                case "create":
                case "update":
                    esSyncService.syncProductToEs(id);
                    break;
                case "delete":
                    esSyncService.deleteFromEs(id);
                    break;
                default:
                    log.warn("未知的同步动作: {}", action);
            }
        } catch (Exception e) {
            log.error("处理ES同步消息失败：{}", message, e);
        }
    }

}
