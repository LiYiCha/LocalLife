package com.coupon.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件名: CouponOrderDTO
 * 创建者: @一茶
 * 创建时间:2025/4/15 18:04
 * 描述：
 */
@Data
public class CouponOrderDTO implements Serializable {
    private Integer couponOrderId;
    private Integer userId;
    private Integer couponId;
    private Byte status;
    private LocalDateTime createdTime;
    private LocalDateTime payTime;
    private LocalDateTime useTime;
}
