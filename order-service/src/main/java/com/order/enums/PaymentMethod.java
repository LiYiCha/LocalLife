package com.order.enums;

public enum PaymentMethod {
    ALIPAY("ALIPAY", "支付宝"),
    WECHAT("WECHAT", "微信支付"),
    UNIONPAY("UNIONPAY", "银联支付");

    private final String code;
    private final String description;

    PaymentMethod(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentMethod fromCode(String code) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.getCode().equals(code)) {
                return method;
            }
        }
        throw new IllegalArgumentException("未知的支付方式: " + code);
    }
}
