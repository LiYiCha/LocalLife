package com.api.payment.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author YC
 * @date 2025/5/22 22:09
 * 支付宝交易状态：
 * WAIT_BUYER_PAY（交易创建，等待买家付款）
 * TRADE_SUCCESS（交易支付成功，可退款）
 * TRADE_FINISHED（交易结束，不可退款）
 * TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）
 */
@Data
public class PaymentStatus implements Serializable {
    private String orderId;         // 商户订单号
    private String paymentId;       // 支付平台订单号
    private String status;          // 支付状态
    private BigDecimal amount;      // 支付金额
    private Date payTime;           // 支付时间
}
