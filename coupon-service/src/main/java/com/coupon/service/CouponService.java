package com.coupon.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.utils.Result;
import com.coupon.pojo.Coupon;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 优惠券表 服务类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
public interface CouponService extends IService<Coupon> {

    Boolean validateCoupon(Integer couponId, Integer userId);

    BigDecimal calculateDiscount(BigDecimal orderAmount, Coupon coupon);

    boolean removeByIdAndStoreId(Integer couponId, Integer storeId);

    Result getCouponsByUser(Integer userId, Integer page, Integer size);

    Result deductCoupon(Integer couponId, Integer userId);
}
