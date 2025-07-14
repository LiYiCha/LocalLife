package com.api.payment.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Data
public class PaymentRequest implements Serializable {
    private String orderId;          // 商户订单号(必填)
    private BigDecimal amount;      // 订单金额(必填)
    private String subject;         // 订单标题(必填)
    private String body;            // 订单描述(可选)
    private Date timeExpire;        // 订单超时时间(可选)
    private String goodsType;       // 商品类型(可选)
    private Map<String, Object> extendParams; // 扩展参数(可选)


    private String clientIp;        // 客户端IP
    private String openId;          // 微信用户openId(仅微信需要)
    private String tradeType;       // 交易类型(APP/WEB等)


    // 支付渠道特定参数
    private String productCode;     // 产品码(如FAST_INSTANT_TRADE_PAY)
    private String authToken;       // 授权令牌(特殊场景使用)
}
