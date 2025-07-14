package com.yc.payment.service;

import com.yc.payment.dto.RefundRequest;
import com.yc.payment.utils.Result;

/**
 * 文件名: WechatRefundService
 * 创建者: @一茶
 * 创建时间:2025/6/2 16:00
 * 描述：
 */
public interface WechatRefundService {
    Result refund(RefundRequest refundRequest);
    Result queryByOutRefundNo(String outRefundNo);
}
