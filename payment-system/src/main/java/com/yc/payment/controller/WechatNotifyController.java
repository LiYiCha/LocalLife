package com.yc.payment.controller;

import com.wechat.pay.java.core.notification.RequestParam;
import com.yc.payment.service.WechatNotifyService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 微信支付异步回调通知控制器
 */
@RestController
@RequestMapping("/wechatpay")
public class WechatNotifyController {
    @GetMapping("/test")
    public String test() {
        return "OK";
    }
//     private final WechatNotifyService wechatNotifyService;
//     public WechatNotifyController(WechatNotifyService wechatNotifyService) {
//        this.wechatNotifyService = wechatNotifyService;
//    }
//    /**
//     * 支付结果异步通知接口
//     */
//    @PostMapping("/notify")
//    public ResponseEntity<String> handleWechatNotify(@RequestHeader("Wechatpay-Serial") String serialNumber,
//                                                     @RequestHeader("Wechatpay-Nonce") String nonce,
//                                                     @RequestHeader("Wechatpay-Signature") String signature,
//                                                     @RequestHeader("Wechatpay-Timestamp") String timestamp,
//                                                     @RequestBody String body,
//                                                     HttpServletRequest request) throws IOException {
//        RequestParam requestParam = new RequestParam.Builder()
//                .serialNumber(serialNumber)
//                .nonce(nonce)
//                .signature(signature)
//                .timestamp(timestamp)
//                .body(body)
//                .build();
//
//        return wechatNotifyService.handleWechatNotify(requestParam);
//    }

}
