package com.yc.payment.controller;

import com.yc.payment.service.AlipayNotifyService;
import com.yc.payment.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 支付宝异步回调接口
 * 文件名: AlipayNotifyController
 * 创建者: @一茶
 * 创建时间:2025/5/22 22:19
 * 描述：
 */

@RestController
@RequestMapping("/api/payment/notify")
public class AlipayNotifyController {

    @Resource
    private AlipayNotifyService  alipayNotifyService;

    /**
     * 支付宝同步回调接口
     * @param request
     * @return
     */
    @GetMapping("/alipay/return")
    public Object alipayReturn(HttpServletRequest request) {
        return alipayNotifyService.alipayReturn(request);
    }

    /**
     * 支付宝异步回调接口
     * @param request
     * @return
     */
    @PostMapping("/alipay/notify")
    public String alipayNotify(HttpServletRequest request) {
        return alipayNotifyService.alipayNotify(request);
    }

}

