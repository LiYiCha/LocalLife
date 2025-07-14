package com.yc.payment.service.impl;

import com.alibaba.nacos.common.utils.StringUtils;
import com.alipay.api.*;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.ExtendParams;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.api.payment.dto.PaymentRequest;
import com.api.payment.dto.RefundRequest;
import com.api.payment.service.AlipayService;
import com.yc.payment.config.AliPayConfig;
import com.yc.payment.config.AlipayCline;
import com.yc.payment.dto.PaymentStatus;
import com.api.utils.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 支付宝支付服务实现类
 * @author yc
 * @description 支付宝支付服务
 * QUICK_WAP_WAY：手机网站快速支付
 * FAST_INSTANT_TRADE_PAY：电脑网站支付
 * QUICK_MSECURITY_PAY : APP支付
 */
@Slf4j
@Service
@DubboService(version = "1.0.0", group = "dev", timeout = 5000,loadbalance = "random",weight = 10)
public class AlipayServiceImpl implements AlipayService {

    private static final String ALIPAY_PRODUCT_CODE = "FAST_INSTANT_TRADE_PAY";
    @Resource
    private AlipayCline alipayCline;

    @Resource
    private AliPayConfig aliPayConfig;




    /**
     * 创建支付宝支付订单
     * @param pr 支付请求参数
     * @return 支付结果
     */
    @Override
    public Object createOrder(PaymentRequest pr) {
        try {
            // 参数校验
            if (StringUtils.isBlank(pr.getOrderId())) {
                return ApiResponse.error("订单号不能为空");
            }
            if (pr.getAmount() == null || pr.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                return ApiResponse.error("支付金额不能小于0");
            }
            if (StringUtils.isBlank(pr.getSubject())) {
                return ApiResponse.error("订单/服务标题不能为空");
            }
            // 创建AlipayClient实例
            AlipayClient alipayClient = alipayCline.createAlipayClient();
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

            // 设置同步/异步通知地址
            request.setReturnUrl(aliPayConfig.getReturnUrl());
            request.setNotifyUrl(aliPayConfig.getNotifyUrl());

            // 构建业务参数模型
            AlipayTradePagePayModel model = new AlipayTradePagePayModel();
            model.setOutTradeNo(pr.getOrderId());
            model.setTotalAmount(pr.getAmount().setScale(2, RoundingMode.HALF_UP).toString());
            model.setSubject(pr.getSubject());
            model.setProductCode(ALIPAY_PRODUCT_CODE); // 电脑网站支付

            // 可选参数设置
            if (StringUtils.isNotBlank(pr.getBody())) {
                model.setBody(pr.getBody()); // 商品描述
            }
            if (pr.getTimeExpire() != null) {
                // 设置订单过期时间
                model.setTimeExpire(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(pr.getTimeExpire()));
            }
            if (StringUtils.isNotBlank(pr.getGoodsType())) {
                // 商品类型
                model.setGoodsType(pr.getGoodsType());
            }
            if (pr.getExtendParams() != null && !pr.getExtendParams().isEmpty()) {
                // 扩展参数
                model.setExtendParams((ExtendParams) convertExtendParams(pr.getExtendParams()));
            }
            // 设置业务参数模型
            request.setBizModel(model);
            // 执行请求
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
            // 处理返回结果
            if (response.isSuccess()) {
                return ApiResponse.success(createResponse(response));
            } else {
                return ApiResponse.error("支付宝下单失败: " + response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            return ApiResponse.error("支付宝接口调用异常: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("系统异常: " + e.getMessage());
        }
    }
    /**
     * 支付宝退款服务
     * @param refundRequest 退款请求参数
     * @return 退款结果
     */
    @Override
    public Object refund(RefundRequest refundRequest) {
        try {
            // 参数校验
            if (StringUtils.isBlank(refundRequest.getOrderId())) {
                return ApiResponse.error("原订单号不能为空");
            }
            if (refundRequest.getRefundAmount() == null || refundRequest.getRefundAmount().compareTo(BigDecimal.ZERO) <= 0) {
                return ApiResponse.error("退款金额必须大于0");
            }

            AlipayClient alipayClient = alipayCline.createAlipayClient();
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();

            // 构建退款业务参数
            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            model.setOutTradeNo(refundRequest.getOrderId()); // 原支付订单号
            model.setRefundAmount(refundRequest.getRefundAmount().setScale(2, RoundingMode.HALF_UP).toString());
            model.setOutRequestNo(generateRefundNo()); // 退款请求号(同一订单多次退款需不同)

            // 可选参数
            if (StringUtils.isNotBlank(refundRequest.getRefundReason())) {
                model.setRefundReason(refundRequest.getRefundReason());
            }
            if (StringUtils.isNotBlank(refundRequest.getOperatorId())) {
                model.setOperatorId(refundRequest.getOperatorId()); // 操作员ID
            }

            request.setBizModel(model);

            // 执行退款请求
            AlipayTradeRefundResponse response = alipayClient.execute(request);

            if (response.isSuccess()) {
                Map<String, Object> result = new HashMap<>();
                result.put("refundAmount", response.getRefundFee());
                result.put("tradeNo", response.getTradeNo());
                result.put("fundChange", response.getFundChange());
                return ApiResponse.success("退款成功", result);
            } else {
                return ApiResponse.error("退款失败: " + response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            return ApiResponse.error("支付宝接口调用异常: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("系统异常: " + e.getMessage());
        }
    }


    /**
     * 查询支付宝订单状态
     * @param orderId
     * @return
     */
    @Override
    public Object queryOrder(String orderId) {
        try {
            AlipayClient alipayClient = alipayCline.createAlipayClient();
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

            AlipayTradeQueryModel model = new AlipayTradeQueryModel();
            model.setOutTradeNo(orderId);
            request.setBizModel(model);

            AlipayTradeQueryResponse response = alipayClient.execute(request);

            PaymentStatus status = new PaymentStatus();
            status.setOrderId(orderId);

            if (response.isSuccess()) {
                status.setOrderId(response.getOutTradeNo());
                status.setPaymentId(response.getTradeNo());
                status.setStatus(response.getTradeStatus());
                status.setDisplayStatus(mapAlipayTradeStatus(response.getTradeStatus()));
                status.setAmount(new BigDecimal(response.getTotalAmount()));
                if (StringUtils.isNotBlank(String.valueOf(response.getSendPayDate()))) {
                    try {
                        // 使用正确格式解析
                        SimpleDateFormat sourceFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                        Date payTime = sourceFormat.parse(String.valueOf(response.getSendPayDate()));

                        // 转换为需要的格式
                        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        status.setPayTimeStr(targetFormat.format(payTime));

                    } catch (Exception e) {
                        log.error("支付时间解析失败：{}", response.getSendPayDate(), e);
                    }
                }
                return ApiResponse.success(status);
            } else {
                return ApiResponse.error("查询订单失败: " + response.getSubMsg());
            }

        } catch (AlipayApiException e) {
            return ApiResponse.error("支付宝接口调用异常: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("系统异常: " + e.getMessage());
        }
    }

    /**
     * 支付宝异步通知验签
     * @param params
     * @return
     */
    public boolean verifyNotify(Map<String, String> params) {
        try {
            if (aliPayConfig.isCertMode()) {
                return AlipaySignature.rsaCertCheckV1(
                        params,
                        ResourceUtils.getFile(aliPayConfig.getAlipayCertPath()).getAbsolutePath(),
                        "UTF-8",
                        aliPayConfig.getSignType());
            } else {
                return AlipaySignature.rsaCheckV1(
                        params,
                        aliPayConfig.getAlipayPublicKey(),
                        "UTF-8",
                        aliPayConfig.getSignType());
            }
        } catch (Exception e) {
            log.error("支付宝通知验签异常", e);
            return false;
        }
    }
    /**
     * 生成退款单号
     */
    private String generateRefundNo() {
        return "RF" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }


    /**
     * 转换扩展参数
     */
    private Map<String, String> convertExtendParams(Map<String, Object> params) {
        Map<String, String> extendParams = new HashMap<>();
        params.forEach((k, v) -> extendParams.put(k, v != null ? v.toString() : ""));
        return extendParams;
    }

    /**
     * 构建响应数据
     */
    private Map<String, Object> createResponse(AlipayTradePagePayResponse response) {
        Map<String, Object> result = new HashMap<>();
        result.put("payUrl", response.getBody()); // 支付页面URL
        result.put("orderId", response.getOutTradeNo()); // 商户订单号
        result.put("tradeNo", response.getTradeNo()); // 支付宝交易号
        return result;
    }

    /**
     * 支付宝交易状态转换
     */
    private String mapAlipayTradeStatus(String tradeStatus) {
        switch (tradeStatus) {
            case "TRADE_SUCCESS":
                return "支付成功";
            case "WAIT_BUYER_PAY":
                return "等待付款";
            case "TRADE_CLOSED":
                return "交易关闭";
            case "TRADE_FINISHED":
                return "交易完成";
            default:
                return "未知状态";
        }
    }

}
