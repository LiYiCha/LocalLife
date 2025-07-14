package com.order.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件名: OrderDetailDTO
 * 创建者: @一茶
 * 创建时间:2025/4/6 16:06
 * 描述：
 */
@Data
public class OrderDetailDTO {
    private Integer orderId; // 订单ID
    private Integer userId; // 用户ID
    private Integer merchantId; // 商家ID
    private String orderNo; // 订单编号
    private BigDecimal totalAmount; // 总金额
    private String status; // 订单状态
    private LocalDateTime createdTime; // 创建时间
    private List<OrderItemDTO> items; // 订单明细
    private PaymentDTO payment; // 支付信息

}
