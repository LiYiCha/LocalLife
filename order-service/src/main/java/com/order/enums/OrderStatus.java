package com.order.enums;

public enum OrderStatus {
    PENDING("PENDING", "等待支付"),
    PAID("PAID", "已支付"),
    CANCELED("CANCELED", "已取消"),
    RETURN("RETURN", "已退货/退款"),
    COMPLETED("COMPLETED", "已完成");

    private final String code;
    private final String description;

    OrderStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static OrderStatus fromCode(String code) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的订单状态: " + code);
    }
}
