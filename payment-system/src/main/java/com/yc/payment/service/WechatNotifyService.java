package com.yc.payment.service;

import com.wechat.pay.java.core.notification.RequestParam;
import org.springframework.http.ResponseEntity;

/**
 * 文件名: WechatNotifyService
 * 创建者: @一茶
 * 创建时间:2025/6/3 8:59
 * 描述：
 */
public interface WechatNotifyService {


    ResponseEntity<String> handleWechatNotify(RequestParam requestParam);
}
