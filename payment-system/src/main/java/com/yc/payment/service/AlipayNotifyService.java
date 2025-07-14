package com.yc.payment.service;


import javax.servlet.http.HttpServletRequest;

/**
 * 文件名: AlipayNotifyService
 * 创建者: @一茶
 * 创建时间:2025/6/7 15:40
 * 描述：
 */
public interface AlipayNotifyService {
    Object alipayReturn(HttpServletRequest request);

    String alipayNotify(HttpServletRequest request);
}
