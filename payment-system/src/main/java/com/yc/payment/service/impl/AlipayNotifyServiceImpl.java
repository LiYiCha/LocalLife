package com.yc.payment.service.impl;

import com.alipay.api.internal.util.AlipaySignature;
import com.yc.payment.config.AliPayConfig;
import com.yc.payment.service.AlipayNotifyService;
import com.yc.payment.utils.RabbitMqUtils;
import com.yc.payment.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件名: AlipayNotifyServiceImpl
 * 创建者: @一茶
 * 创建时间:2025/6/7 15:41
 * 描述：
 *  如果是调用的 SDK，非证书方式请求是使用的 rsaCheckV1 方法，证书方式请求使用 rsaCertCheckV1 方法进行验签。
 *  接口记得是公开的，不要验证token之类的，防止支付宝/微信请求失败。
 */
@Service
@Slf4j
public class AlipayNotifyServiceImpl implements AlipayNotifyService {

    @Resource
    private AliPayConfig aliPayConfig;
    @Resource
    private RabbitMqUtils rabbitMqUtils;

    /**
     * 支付宝同步回调接口
     *
     * @param request
     * @return
     */
    @Override
    public Object alipayReturn(HttpServletRequest request) {
        try {
            Map<String, String> params = new HashMap<>();
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String name = parameterNames.nextElement();
                params.put(name, request.getParameter(name));
            }

            // 验证签名
            boolean signVerified = AlipaySignature.rsaCheckV1(
                    params,
                    aliPayConfig.getAlipayPublicKey(),
                    "UTF-8",
                    aliPayConfig.getSignType());

            if (signVerified) {
                String tradeStatus = params.get("trade_status");
                String outTradeNo = params.get("out_trade_no");
                String tradeNo = params.get("trade_no");
                String totalAmount = params.get("total_amount");

                // 这里可以记录日志或更新订单状态
                log.info("支付宝同步回调验证成功，订单状态：{}，订单号：{}，支付宝交易号：{}，金额：{}",
                        tradeStatus,outTradeNo, tradeNo, totalAmount);

                // 跳转到支付成功页面
                return Result.success(outTradeNo);
            } else {
                log.error("支付宝同步回调签名验证失败");
                return Result.error("支付宝同步回调签名验证失败");
            }
        } catch (Exception e) {
            log.error("处理支付宝同步回调异常", e);
            return Result.error("处理支付宝同步回调异常");
        }
    }


    /**
     * 支付宝异步回调接口
     *
     * @param request
     * @return
     */
    @Override
    public String alipayNotify(HttpServletRequest request) {
        try {
            Map<String, String> params = new HashMap<>();
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String name = parameterNames.nextElement();
                params.put(name, request.getParameter(name));
            }

            // 验证签名
            boolean signVerified;
            if (aliPayConfig.isCertMode()) {
                signVerified = AlipaySignature.rsaCertCheckV1(
                        params,
                        ResourceUtils.getFile(aliPayConfig.getAlipayCertPath()).getAbsolutePath(),
                        "UTF-8",
                        aliPayConfig.getSignType());
            } else {
                signVerified = AlipaySignature.rsaCheckV1(
                        params,
                        aliPayConfig.getAlipayPublicKey(),
                        "UTF-8",
                        aliPayConfig.getSignType());
            }

            if (!signVerified) {
                log.error("支付宝异步通知验签失败");
                return "failure";
            }

            // 交易状态
            String tradeStatus = params.get("trade_status");

            if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                String outTradeNo = params.get("out_trade_no");
                String tradeNo = params.get("trade_no");
                String totalAmount = params.get("total_amount");

                Map<String, Object> notifyParamsMap = new HashMap<>();
                notifyParamsMap.put("out_trade_no", outTradeNo);
                notifyParamsMap.put("trade_no", tradeNo);
                notifyParamsMap.put("total_amount", totalAmount);

                log.info("支付宝支付成功，订单号：{}，支付宝交易号：{}，金额：{}", outTradeNo, tradeNo, totalAmount);
                // mq发送消息 - 需要异步处理，防止超时响应支付宝,3秒内
                asyncSendPaymentSuccess(notifyParamsMap);

                return "success";
            }

            return "failure";
        } catch (Exception e) {
            log.error("处理支付宝异步通知异常", e);
            return "failure";
        }
    }

    @Async
    public void asyncSendPaymentSuccess(Map<String, Object> notifyParams) {
        try {
            rabbitMqUtils.sendJson("order_payment_success_queue", notifyParams);
        } catch (Exception e) {
            log.error("发送支付成功消息失败", e);
        }
    }
}
