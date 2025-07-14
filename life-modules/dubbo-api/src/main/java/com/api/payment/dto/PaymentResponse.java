package com.api.payment.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class PaymentResponse implements Serializable {
    private boolean success;        // 是否成功
    private String paymentId;       // 支付平台订单号
    private String prepayId;        // 预支付ID(微信)
    private String payUrl;          // 支付链接
    private String qrCodeUrl;       // 二维码链接
    private Map<String, String> extraParams; // 额外参数
}
