package com.coupon.service;

import com.coupon.pojo.CouponOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 优惠券订单表 服务类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
public interface CouponOrderService extends IService<CouponOrder> {

    Boolean validateCouponOrder(Integer couponId, Integer userId);

    int userCoupon(Integer couponId, Integer userId);

}
