package com.yc.payment.service.impl;

import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.partnerpayments.jsapi.model.Transaction;
import com.yc.payment.service.WechatNotifyService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@ConditionalOnProperty(name = "wechat.enabled", havingValue = "true", matchIfMissing = false)
public class WechatNotifyServiceImpl implements WechatNotifyService {

    private final NotificationParser notificationParser;

    public WechatNotifyServiceImpl(NotificationParser notificationParser) {
        this.notificationParser = notificationParser;
    }

    /**
     * 处理微信支付异步回调
     */
    @Override
    public ResponseEntity<String> handleWechatNotify(RequestParam requestParam) {
        Transaction transaction;
        try {
            // 解析并验证回调数据
            transaction = notificationParser.parse(requestParam, Transaction.class);
        } catch (Exception e) {
            // 签名验证失败
            return ResponseEntity.status(401).body("签名失败");
        }

        if (Objects.isNull(transaction)) {
            return ResponseEntity.badRequest().body("解析失败");
        }

        // 处理支付成功逻辑
        if ("SUCCESS".equals(transaction.getTradeState())) {
            boolean success = true;

            if (!success) {
                return ResponseEntity.status(500).body("处理失败");
            }
        }

        return ResponseEntity.ok("OK");
    }
}
