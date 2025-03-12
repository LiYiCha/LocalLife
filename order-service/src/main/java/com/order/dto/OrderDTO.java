package com.order.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO {
    private Integer userId;
    private Integer couponId; // 使用的优惠券ID
    private Integer addressId; // 收货地址ID
    private String remark; // 订单备注
    private List<OrderItemDTO> items; // 订单商品项
    private BigDecimal totalAmount; // 订单总金额
    private BigDecimal discountAmount; // 优惠金额
    private BigDecimal payAmount; // 实际支付金额
}
