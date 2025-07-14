package com.order.utils;

import com.alibaba.fastjson.JSON;
import com.core.utils.MD5Utils;
import com.order.dto.OrderDTO;
import com.order.dto.OrderItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 文件名: OrderUtils
 * 创建者: @一茶
 * 创建时间:2025/5/13 15:25
 * 描述：
 */
@Component
public class OrderUtils {
    private static final int LOCK_POOL_SIZE = 10;  // 分段锁锁槽位数量


    // 使用一致性哈希来减少冲突概率
    public String generateLockKey(String baseKey, Integer id) {
        return baseKey + "_" + (Math.abs(id.hashCode() % LOCK_POOL_SIZE));
    }
    // 辅助方法：生成订单内容的唯一标识符
    public String generateOrderContentHash(OrderDTO orderDTO) {
        // 使用 MD5 哈希生成唯一标识符
        String orderContent = JSON.toJSONString(orderDTO);
        return MD5Utils.encrypt(orderContent);
    }




    // 辅助方法：计算总金额
    public BigDecimal calculateTotalAmount(List<OrderItemDTO> items) {
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())).setScale(2, RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    // 辅助方法：生成订单号
    public String generateOrderNo() {
        return UUID.randomUUID().toString();
    }

    // 辅助方法：生成支付流水号
    public String generatePaymentNo() {
        return "P" + System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

}
