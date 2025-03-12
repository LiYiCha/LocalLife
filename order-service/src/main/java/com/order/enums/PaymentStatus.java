package com.order.enums;

public enum PaymentStatus {
    UNPAID("UNPAID", "未支付"),
    PAID("PAID", "已支付"),
    CANCELED("CANCELED", "已取消"),
    REFUNDED("REFUNDED", "已退款");

    private final String code;
    private final String description;

    PaymentStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentStatus fromCode(String code) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的支付状态: " + code);
    }
}
