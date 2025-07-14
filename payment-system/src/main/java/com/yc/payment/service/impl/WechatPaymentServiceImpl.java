package com.yc.payment.service.impl;

import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.yc.payment.config.WeChatPayConfig;
import com.yc.payment.dto.PaymentRequest;
import com.yc.payment.service.WechatPaymentService;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 微信支付服务实现类
 * @author yc
 * @date 2020/5/27 16:04
 * @description 微信支付服务
 * JSAPI ： 公众号支付、小程序支付、APP支付、H5支付
 * NATIVE：扫码支付、原生扫码支付
 * APP：APP支付
 * MWEB：H5支付（手机网站支付）
 *
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "wechat.enabled", havingValue = "true", matchIfMissing = false)
public class WechatPaymentServiceImpl implements WechatPaymentService {


    private final JsapiService jsapiService;
    private final WeChatPayConfig wechatPayConfig;

    public WechatPaymentServiceImpl(JsapiService jsapiService, WeChatPayConfig wechatPayConfig) {
        this.jsapiService = jsapiService;
        this.wechatPayConfig = wechatPayConfig;
    }

    /** JSAPI支付下单，并返回JSAPI调起支付数据 */
    @Override
    public String createJsapiOrder(PaymentRequest pr) {
        PrepayRequest request = new PrepayRequest();

        // 设置金额
        BigDecimal amountInYuan = pr.getAmount();
        // 转换为分，并校验是否超出 Integer 范围。使用 .intValueExact() 方法，当出现精度丢失时直接抛异常。
        int amountInCent;
        try {
            amountInCent = amountInYuan.multiply(new BigDecimal(100)).intValueExact();
        } catch (ArithmeticException e) {
            log.error("金额必须为整数分，不能有小数");
            throw new IllegalArgumentException("金额必须为精确到分的数值");
        }
        Amount orderAmount = new Amount();
        orderAmount.setTotal(amountInCent);//单位为分
        orderAmount.setCurrency("CNY");

        // 设置支付者
        Payer payer = new Payer();
        payer.setOpenid(pr.getOpenId());

        // 构建请求
        request.setAppid(wechatPayConfig.getAppId());
        request.setMchid(wechatPayConfig.getMchId());
        request.setDescription(pr.getSubject());
        request.setOutTradeNo(pr.getOrderId());
        request.setNotifyUrl(wechatPayConfig.getNotifyUrl());
        request.setAmount(orderAmount);
        request.setPayer(payer);

        // 调用JSAPI支付接口
        PrepayResponse response = jsapiService.prepay(request);
        return response.getPrepayId();
    }



    /**
     * 关闭订单
     * @param outTradeNo 商户订单号
     * @return 是否关闭成功
     */
    @Override
    public boolean closeOrder(String outTradeNo) {
        CloseOrderRequest request = new CloseOrderRequest();
        request.setMchid(wechatPayConfig.getMchId());
        request.setOutTradeNo(outTradeNo);

        try {
            jsapiService.closeOrder(request);
            return true;
        } catch (Exception e) {
            log.error("关闭微信订单失败，订单号: {}", outTradeNo, e);
            return false;
        }
    }



    /**
     * 根据微信支付订单号查询订单
     * @param transactionId 微信支付订单号
     * @return 订单详情
     */
    @Override
    public Transaction queryOrderById(String transactionId) {
        QueryOrderByIdRequest request = new QueryOrderByIdRequest();
        request.setTransactionId(transactionId);

        return jsapiService.queryOrderById(request);
    }


    /**
     * 根据商户订单号查询订单
     * @param outTradeNo 商户订单号
     * @return 订单详情
     */
    @Override
    public Transaction queryOrderByOutTradeNo(String outTradeNo) {
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setMchid(wechatPayConfig.getMchId());
        request.setOutTradeNo(outTradeNo);

        return jsapiService.queryOrderByOutTradeNo(request);
    }

    /**
     * 查询订单
     * @param outTradeNo 商户订单号
     * @return
     */
    @Override
    public Transaction queryOrder(String outTradeNo) {
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setMchid(wechatPayConfig.getMchId());
        request.setOutTradeNo(outTradeNo);

        return jsapiService.queryOrderByOutTradeNo(request);
    }
}
