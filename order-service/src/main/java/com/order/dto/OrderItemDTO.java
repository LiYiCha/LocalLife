package com.order.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Integer productId; // 商品ID
    private Integer quantity;  // 购买数量
    private BigDecimal price;  // 单价
    private String productName; // 商品名称
}
