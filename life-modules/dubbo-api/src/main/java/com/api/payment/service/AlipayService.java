package com.api.payment.service;

import com.api.payment.dto.PaymentRequest;
import com.api.payment.dto.RefundRequest;


/**
 * 文件名: AlipayServiceImpl
 * 创建者: @一茶
 * 创建时间:2025/6/7 16:54
 * 描述：
 */
public interface AlipayService {

    Object createOrder(PaymentRequest paymentRequest);

    Object refund(RefundRequest refundRequest);

    Object queryOrder(String orderId);
}
