package com.coupon.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coupon.dto.CouponDetailDTO;
import com.coupon.pojo.Coupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coupon.pojo.CouponOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 优惠券表 Mapper 接口
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
public interface CouponMapper extends BaseMapper<Coupon> {

    List<CouponDetailDTO> getCouponAndOrder(@Param("userId")Integer userId, @Param("page") Page<CouponDetailDTO> page);
}
