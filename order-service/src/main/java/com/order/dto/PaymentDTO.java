package com.order.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 文件名: PaymentDTO
 * 创建者: @一茶
 * 创建时间:2025/4/6 16:07
 * 描述：
 */
@Data
public class PaymentDTO {
    private String paymentNo; // 支付流水号
    private BigDecimal amount; // 支付金额
    private String status; // 支付状态
    private String paymentMethod; // 支付方式
}
