package com.api.payment.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class RefundRequest implements Serializable {
    private String orderId;          // 原支付订单号(必填)
    private BigDecimal refundAmount; // 退款金额(必填)
    private String refundReason;     // 退款原因(可选)
    private String operatorId;       // 操作员ID(可选)
    private String outRequestNo;     // 退款请求号(可选，不传则自动生成)
}
