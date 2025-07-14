package com.api.payment.service;


import javax.servlet.http.HttpServletRequest;

/**
 * 文件名: AlipayNotifyService
 * 创建者: @一茶
 * 创建时间:2025/6/7 16:50
 * 描述：
 */
public interface AlipayNotifyService {
    Object alipayReturn(HttpServletRequest request);

    String alipayNotify(HttpServletRequest request);
}
