package com.yc.payment.service;

import com.wechat.pay.java.service.payments.model.Transaction;
import com.yc.payment.dto.PaymentRequest;


public interface WechatPaymentService {

    String createJsapiOrder(PaymentRequest pr);

    boolean closeOrder(String outTradeNo);

    Transaction queryOrderById(String transactionId);

    Transaction queryOrderByOutTradeNo(String outTradeNo);

    Transaction queryOrder(String outTradeNo);
}
