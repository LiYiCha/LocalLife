package com.yc.payment.service.impl;

import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.*;
import com.yc.payment.config.WeChatPayConfig;
import com.yc.payment.dto.RefundRequest;
import com.yc.payment.service.WechatRefundService;
import com.yc.payment.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 文件名: WechatRefundServiceImpl
 * 创建者: @一茶
 * 创建时间:2025/6/2 15:57
 * 描述：微信退款服务实现类
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "wechat.enabled", havingValue = "true", matchIfMissing = false)
public class WechatRefundServiceImpl implements WechatRefundService {
    private final WeChatPayConfig wechatPayConfig;
    private final RefundService refundService;

    public WechatRefundServiceImpl(WeChatPayConfig wechatPayConfig,RefundService refundService) {
        this.wechatPayConfig = wechatPayConfig;
        this.refundService = refundService;
    }



    /** 退款申请 */
    /**
     * 退款申请
     */
    public Result refund(RefundRequest refundRequest) {
        if (refundRequest == null || refundRequest.getOrderId() == null || refundRequest.getRefundAmount() == null) {
            log.error("退款请求参数缺失");
            throw new IllegalArgumentException("退款请求参数缺失");
        }

        CreateRequest request = new CreateRequest();

        // 原支付交易号（必填）
        request.setTransactionId(refundRequest.getOrderId());

        // 商户退款单号（可选，不传则自动生成）
        if (refundRequest.getOutRequestNo() != null) {
            request.setOutRefundNo(refundRequest.getOutRequestNo());
        } else {
            request.setOutRefundNo("R" + System.currentTimeMillis()); // 自定义生成规则
        }

        // 设置退款金额（转换为分）
        BigDecimal refundAmount = refundRequest.getRefundAmount();
        int refundAmountInCent;
        try {
            refundAmountInCent = refundAmount.multiply(new BigDecimal(100)).intValueExact();
        } catch (ArithmeticException e) {
            log.error("退款金额必须为整数分，不能有小数");
            throw new IllegalArgumentException("退款金额必须为精确到分的数值");
        }

        // 构建退款金额对象
        AmountReq amountReq = new AmountReq();
        amountReq.setRefund((long) refundAmountInCent); // 单位是分，强制转 long
        amountReq.setTotal((long) refundAmountInCent);  // 原订单金额也应为 long
        amountReq.setCurrency("CNY");

        //  设置退款金额对象
        request.setAmount(amountReq);

        // 设置退款原因（可选）
        if (refundRequest.getRefundReason() != null) {
            request.setReason(refundRequest.getRefundReason());
        }

        // 发起退款请求
        Refund refund;
        try {
            refund = refundService.create(request);
        } catch (Exception e) {
            log.error("微信退款调用失败: {}", e.getMessage(), e);
            return Result.error("微信退款调用失败");
        }

        // 处理退款结果
        Status refundStatus = refund.getStatus();
        if (Status.SUCCESS == refundStatus) {
            return Result.success("退款成功", refund);
        } else if (Status.PROCESSING == refundStatus) {
            return Result.success("退款处理中", refund);
        } else {
            log.error("退款失败详情: {}", refund);
            return Result.error("退款失败,请联系客服!");
        }

    }


    /**
     * 查询单笔退款（通过商户退款单号）
     */
    public Result queryByOutRefundNo(String outRefundNo) {
        if (outRefundNo == null || outRefundNo.isEmpty()) {
            throw new IllegalArgumentException("商户退款单号不能为空");
        }

        QueryByOutRefundNoRequest request = new QueryByOutRefundNoRequest();
        request.setSubMchid(wechatPayConfig.getMchId());
        request.setOutRefundNo(outRefundNo);

        Refund refund;
        try {
            refund = refundService.queryByOutRefundNo(request);
        } catch (Exception e) {
            log.error("查询退款失败: {}", e.getMessage(), e);
            return Result.error("查询退款失败");
        }

        return Result.success(refund);
    }

}
